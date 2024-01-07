package CENG453.group18.entity;

//import CENG453.group18.dictionary.GameBoardDictionary;
import CENG453.group18.dictionary.NodeDictionaryObject;
import CENG453.group18.enums.CardType;
import CENG453.group18.enums.GameType;

//import CENG453.group18.repository.PlayerRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import org.antlr.v4.runtime.misc.Pair; 
// import org.checkerframework.checker.units.qual.C; 

import java.util.ArrayList;
import java.util.List;
//import java.util.Random;
import java.util.Map;
//import java.util.HashMap;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Game {

    // store it in the frontend and use it when sending api requests
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameID;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private GameBoard gameboard;

    @OneToOne
    @JoinColumn(name = "player1")
    Player player1;
    @OneToOne
    @JoinColumn(name = "player2")
    Player player2;
    @OneToOne
    @JoinColumn(name = "player3")
    Player player3;
    @OneToOne
    @JoinColumn(name = "player4")
    Player player4;

    @Column(name = "player1-score")
    Integer player1Score;
    @Column(name = "player2-score")
    Integer player2Score;
    @Column(name = "player3-score")
    Integer player3Score;
    @Column(name = "player4-score")
    Integer player4Score;
    // whose turn is now. if the player-1 is playing, then turn is 1 ...
    // if the player-4 is playing, then turn is 4, and again it's player-1's turn and turn is 1
    // don't forget to make a single player authorized to change game until it's turn finishes
    @Column(name = "turn")
    private Integer turn;

    // SinglePlayer or MultiPlayer
    @Column(name = "game_type")
    private GameType gameType = GameType.SinglePlayer; // default value

    @Column(name = "current_dice1")
    private Integer currentDice1;
    @Column(name = "current_dice2")
    private Integer currentDice2;

    // holds a single card deck for each player and each card deck holds multiple cards belonging to player
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCardDeck> playerCardDeckList = new ArrayList<>();

    @Column(name = "current_longest_road_length")
    private Integer currentLongestRoadLength;
    @Column(name = "current_longest_road_owner_playerNo")
    private Integer currentLongestRoadOwnerPlayerNo;

    // whether the dice is rolled for this turn
    @Column(name = "diceRolled")
    private Boolean diceRolled;

    @Transient
    private List<TradeOffer> tradeOffers;

    public Game(Player player1, Player player2, Player player3, Player player4, GameType gameType) {
        this.gameboard = new GameBoard();
        this.turn = 1;
        this.gameType = gameType;
        this.player1Score = 1;
        this.player2Score = 1;
        this.player3Score = 1;
        this.player4Score = 1;
        this.currentLongestRoadLength = 0;
        this.currentLongestRoadOwnerPlayerNo = 0;
        this.diceRolled = false;
        if(gameType == GameType.SinglePlayer) {
            this.player1 = player1;
        }
        else{
            this.player1 = player1;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
        }

        this.tradeOffers = new ArrayList<>();

        initializePlayerCardDecks();
        distributeInitialCards();
    }

    private void initializePlayerCardDecks() {
        CardType[] cardTypes = CardType.values();

        for(int i = 0; i < 4; i++) {
            PlayerCardDeck playerCardDeck = new PlayerCardDeck();
            for (CardType cardType : cardTypes) {
                playerCardDeck.addCard(new Card(cardType));
            }
            playerCardDeckList.add(playerCardDeck);
        }
    }

    public Integer getTurn() {
        return this.turn;
    }

    /*public int getBotCount() {
        int botCount = 0;
        int currentTurn = getTurn();

        Player[] players = new Player[]{player1, player2, player3, player4};

        for (int i = (currentTurn + 1) % players.length; i != currentTurn; i = (i + 1) % players.length) {
            Player player = players[i];
            if (player == null) {
                botCount++;
            }
        }

        return botCount;
    }*/

    private void distributeInitialCards() {
        // cards from tiles
        for(int i = 2; i < 13; i++) {
            distributeAllCards(i);
        }
        // cards from initial settings
        for(int i = 0; i<4; i++)
        {
            playerCardDeckList.get(i).incrementResourceCounts(CardType.LUMBER, 3);
            playerCardDeckList.get(i).incrementResourceCounts(CardType.BRICK, 3);
            playerCardDeckList.get(i).incrementResourceCounts(CardType.GRAIN, 1);
            playerCardDeckList.get(i).incrementResourceCounts(CardType.WOOL, 1);
        }
    }
    public int createTradeOffer(int playerNo, Map<CardType, Integer> offered, Map<CardType, Integer> requested) {
        // Check if it's the player's turn
        if (playerNo != this.turn) {
            throw new IllegalStateException("It's not Player " + playerNo + "'s turn.");
        }

        TradeOffer tradeOffer = new TradeOffer(playerNo, offered, requested);
        this.tradeOffers.add(tradeOffer);

        // Create a string representation of the offered and requested cards
        String offeredString = offered.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(", "));
        String requestedString = requested.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(", "));

        // Add an event to the game board
        gameboard.addEvent("Player " + playerNo + " created a trade offer. Offered: " + offeredString + ". Requested: " + requestedString);

        return tradeOffer.getTradeOfferID();
    }
    
    public boolean acceptTradeOffer(int playerNo, int tradeOfferID) {
        // Find the trade offer with the given ID
        TradeOffer tradeOffer = this.tradeOffers.stream()
            .filter(offer -> offer.getTradeOfferID() == tradeOfferID)
            .findFirst()
            .orElse(null);

        // If no trade offer with the given ID was found, return false
        if (tradeOffer == null) {
            return false;
        }

        // Get the player's cards
        Map<CardType, Integer> playerCards = playerCardDeckList.get(playerNo - 1).getResourceCounts();

        // Check if the player has enough cards to accept the trade offer
        if (!hasEnoughCards(playerCards, tradeOffer.getRequested())) {
            return false;
        }

        // Get the cards of the player who created the trade offer
        Map<CardType, Integer> offerCreatorCards = playerCardDeckList.get(tradeOffer.getPlayerNo() - 1).getResourceCounts();

        // Check if the player who created the trade offer has enough cards to fulfill the offer
        if (!hasEnoughCards(offerCreatorCards, tradeOffer.getOffered())) {
            return false;
        }

        // Both players have enough cards to proceed with the trade
        // Subtract the requested cards from the player's cards and add the offered cards
        updatePlayerCards(playerNo, tradeOffer.getRequested(), tradeOffer.getOffered());

        // Subtract the offered cards from the offer creator's cards and add the requested cards
        updatePlayerCards(tradeOffer.getPlayerNo(), tradeOffer.getOffered(), tradeOffer.getRequested());

        // Remove the trade offer from the list
        this.tradeOffers.remove(tradeOffer);

        // Add an event to the game board
        gameboard.addEvent("Player " + playerNo + " accepted trade offer " + tradeOfferID + ".");

        // Return true to indicate that the trade offer was accepted
        return true;
    }

    private void updatePlayerCards(int playerNo, Map<CardType, Integer> subtractCards, Map<CardType, Integer> addCards) {
        // Get the PlayerCardDeck for the specified player
        PlayerCardDeck playerDeck = playerCardDeckList.get(playerNo - 1);

        // For each card type in the subtractCards map, decrement the count in the player's deck
        subtractCards.forEach((cardType, count) ->
            playerDeck.decrementResourceCounts(cardType, count));

        // For each card type in the addCards map, increment the count in the player's deck
        addCards.forEach((cardType, count) ->
            playerDeck.incrementResourceCounts(cardType, count));
    }

    public boolean cheat(int playerNo, Map<CardType, Integer> requested) {
        // Get the PlayerCardDeck for the specified player
        PlayerCardDeck playerDeck = playerCardDeckList.get(playerNo - 1);

        // For each requested resource, increment the count in the player's deck
        requested.forEach((cardType, count) ->
            playerDeck.incrementResourceCounts(cardType, count));
        
        // Return true to indicate that the cheat was successful
        return true;
    }

    private boolean hasEnoughCards(Map<CardType, Integer> playerCards, Map<CardType, Integer> requestedCards) {
        return requestedCards.entrySet().stream()
            .allMatch(entry -> playerCards.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    public boolean deleteTradeOffer(int tradeOfferID) {
        // Find the trade offer with the given ID
        for (TradeOffer tradeOffer : this.tradeOffers) {
            if (tradeOffer.getTradeOfferID() == tradeOfferID) {
                // Remove the trade offer from the list
                this.tradeOffers.remove(tradeOffer);

                // Add an event to the game board
                gameboard.addEvent("Trade offer " + tradeOfferID + " was deleted.");

                // Return true to indicate that the trade offer was found and deleted
                return true;
            }
        }

        // Return false to indicate that no trade offer with the given ID was found
        return false;
    }


    // Method to check if a player has enough resources to build a settlement
    public boolean hasEnoughResourcesForSettlement(int playerNo) {
        // Get the resource counts for the player
        Map<CardType, Integer> resourceCounts = playerCardDeckList.get(playerNo - 1).getResourceCounts();
    
        // Check if the player has at least 1 Lumber, 1 Brick, 1 Grain, and 1 Wool
        // Return true if the player has enough resources, false otherwise
        return resourceCounts.getOrDefault(CardType.LUMBER, 0) >= 1 &&
               resourceCounts.getOrDefault(CardType.BRICK, 0) >= 1 &&
               resourceCounts.getOrDefault(CardType.GRAIN, 0) >= 1 &&
               resourceCounts.getOrDefault(CardType.WOOL, 0) >= 1;
    }

    // Method to check if a player has enough resources to build a road
    public boolean hasEnoughResourcesForRoad(int playerNo) {
        // Get the resource counts for the player
        Map<CardType, Integer> resourceCounts = playerCardDeckList.get(playerNo - 1).getResourceCounts();

        // Check if the player has at least 1 Lumber and 1 Brick
        // Return true if the player has enough resources, false otherwise
        return resourceCounts.getOrDefault(CardType.LUMBER, 0) >= 1 &&
               resourceCounts.getOrDefault(CardType.BRICK, 0) >= 1;
    }

    // Method to check if a player has enough resources to build a city
    public boolean hasEnoughResourcesForUpgrade(int playerNo) {
        // Get the resource counts for the player
        Map<CardType, Integer> resourceCounts = playerCardDeckList.get(playerNo - 1).getResourceCounts();
    
        // Check if the player has at least 3 Ores and 2 Grains
        // Return true if the player has enough resources, false otherwise
        return resourceCounts.getOrDefault(CardType.ORE, 0) >= 3 &&
               resourceCounts.getOrDefault(CardType.GRAIN, 0) >= 2;
    }


    /* 
    private void endGame()
    {

    }
    */

    // once the dice is rolled add cards to card decks of players, depend on their settlement placements
    public void distributeAllCards(int currentDice)
    {
        for(int i=0; i<gameboard.getTiles().size(); i++)
        {
            if(gameboard.getTiles().get(i).getTileNumber() == currentDice)
            {
                distributeCardsToAdjacentSettlements(i);
            }
        }
    }

    private void distributeCardsToAdjacentSettlements(int tileIndex)
    {
        Tile tile = gameboard.getTiles().get(tileIndex);
        CardType cardType = switch (tile.getTileType()) {
            case FIELDS -> CardType.GRAIN;
            case FOREST -> CardType.LUMBER;
            case HILLS -> CardType.BRICK;
            case MOUNTAINS -> CardType.ORE;
            default -> CardType.WOOL;
        };

        for (int i=0; i<gameboard.getSettlements().size(); i++)
        {
            if(isSettlementAdjacentToTile(gameboard.getSettlements().get(i).getNodeIndex(), tileIndex))
            {
                Settlement settlement = gameboard.getSettlements().get(i);
                int settlementOwnerPlayerNo = settlement.getPlayerNo();
                if(settlement.getSettlementLevel() == 1)
                {
                    playerCardDeckList.get(settlementOwnerPlayerNo-1).incrementResourceCounts(cardType ,1);
                }
                else
                {
                    playerCardDeckList.get(settlementOwnerPlayerNo-1).incrementResourceCounts(cardType,2);
                }
            }
        }
    }

    private boolean isSettlementAdjacentToTile(int nodeIndex, int tileIndex)
    {
        NodeDictionaryObject nodeDictionaryObject = GameBoard.gameBoardDictionary.getNode(nodeIndex);
        return nodeDictionaryObject.getAdjacentTiles().contains(tileIndex);
    }

    public void consumeResourceCards(String buildType, int playerNo) {
        switch (buildType)
        {
            case "settlement":
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.LUMBER,1);
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.BRICK,1);
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.GRAIN,1);
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.WOOL,1);
                break;
            case "road":
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.LUMBER,1);
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.BRICK,1);
                break;
            case "upgrade":
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.ORE,3);
                playerCardDeckList.get(playerNo - 1).decrementResourceCounts(CardType.GRAIN,2);
                break;
        }
    }

    // once the player have the longest road or built a settlement or city increment its score
    public void incrementPlayerScore(int playerNo, int score)
    {
        switch (playerNo)
        {
            case 1:
                player1Score += score;
                break;
            case 2:
                player2Score += score;
                break;
            case 3:
                player3Score += score;
                break;
            case 4:
                player4Score += score;
                break;
            default:
                break;
        }
    }
    // once the player have the longest road or built a settlement or city increment its score
    public void decrementPlayerScore(int playerNo, int score)
    {
        switch (playerNo)
        {
            case 1:
                player1Score -= score;
                break;
            case 2:
                player2Score -= score;
                break;
            case 3:
                player3Score -= score;
                break;
            case 4:
                player4Score -= score;
                break;
            default:
                break;
        }
    }
    public void setLongestRoadInTheGame()
    {
        int tempCurrentLongestRoadLength = 0;
        int tempCurrentLongestRoadOwnerPlayerNo = 0;
        int tempLength = 0;
        for(int i=1 ; i<5; i++)
        {
            tempLength = gameboard.findLongestRoadLengthOfPlayer(i);
            if(tempLength > tempCurrentLongestRoadLength)
            {
                tempCurrentLongestRoadLength = tempLength;
                tempCurrentLongestRoadOwnerPlayerNo = i;
            }
        }
        // increment the score of the current owner of the longest road
        if(tempCurrentLongestRoadLength >= 5 && currentLongestRoadOwnerPlayerNo != tempCurrentLongestRoadOwnerPlayerNo)
        {
            // if the previous owner's road length is >= threshold, then decrement its score since it was
            // getting 2 points from it, but it is not the owner of the longest road anymore
            if(currentLongestRoadLength >= 5)
            {
                decrementPlayerScore(currentLongestRoadOwnerPlayerNo, 2);
            }
            // transfer score to the new owner
            incrementPlayerScore(tempCurrentLongestRoadOwnerPlayerNo, 2);
            gameboard.addEvent("The owner of the longest road changed from player " + currentLongestRoadOwnerPlayerNo + " to player " + tempCurrentLongestRoadOwnerPlayerNo);
        }
        if(currentLongestRoadLength != tempCurrentLongestRoadLength) {
            gameboard.addEvent("The longest road length changed from " + currentLongestRoadLength + " to " + tempCurrentLongestRoadLength);
        }
        currentLongestRoadLength = tempCurrentLongestRoadLength;
        currentLongestRoadOwnerPlayerNo = tempCurrentLongestRoadOwnerPlayerNo;
    }
}
