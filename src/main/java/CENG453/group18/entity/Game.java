package CENG453.group18.entity;


import CENG453.group18.dictionary.GameBoardDictionary;
import CENG453.group18.dictionary.NodeDictionaryObject;
import CENG453.group18.enums.CardType;
import CENG453.group18.enums.GameType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import org.antlr.v4.runtime.misc.Pair; 
// import org.checkerframework.checker.units.qual.C; 

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;


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



    // These containers are not the correct implementation make it be stored in the database,
    // The idea is simple: the game will hold all of its players reference
    // and once the game is finished the local scores will be added to player's total
    // score to be displayed in th scoreboard

    private int[] playerIDs = new int[4];
    private int[] scores = new int[4];

    // whose turn is now. if the player-1 is playing, then turn is 1 ...
    // if the player-4 is playing, then turn is 4, and again it's player-1's turn and turn is 1
    // don't forget to make a single player authorized to change game until it's turn finishes
    @Column(name = "turn")
    private Integer turn;

    // SinglePlayer or Multiplayer
    @Column(name = "game_type")
    private GameType gameType = GameType.SinglePlayer; // default value

    @Column(name = "current_dice")
    private Integer currentDice;

    // holds a single card deck for each player and each card deck holds multiple cards belonging to player
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCardDeck> playerCardDeckList = new ArrayList<>();

    @Column(name = "current_longest_road_length")
    private Integer currentLongestRoadLength;
    @Column(name = "current_longest_road_owner_playerNo")
    private Integer currentLongestRoadOwnerPlayerNo;



    public Game(int playerID, GameType gameType) {
        this.gameboard = new GameBoard();
        this.turn = 1;
        this.gameType = gameType;
        this.playerIDs = new int[4];
        
        if(gameType == GameType.SinglePlayer) {
            this.playerIDs[0] = playerID;
        }

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

    private void distributeInitialCards() {
        for(int i = 2; i < 13; i++) {
            distributeAllCards(i);
        }
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }


    // Instead of assigning random value here it can be changed this way:
    // Roll 2 rice, get sum of them and assign the value here
    // In current implementation: roll the dice, save value, assign a pair of values
    // total of which is equal to saved value.
    public Integer rollTheDice()
    {
        Random rand = new Random();
        currentDice = rand.nextInt(2,13);
        distributeAllCards(currentDice);
        return currentDice;
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

    /*public void botPlay(int playerNo) {
        // Print the current dice roll
        System.out.println(currentDice);

        // Check if the bot has enough resources to build a settlement
        if (hasEnoughResourcesForSettlement(playerNo)) {
            // Find a suitable location to place a new settlement
            Integer settlementLocation = this.gameboard.findAppropriateSettlementPlacement(playerNo);

            // If a suitable location is found, build a settlement there
            if (settlementLocation != null) {
                // Call the game service to add a settlement at the found location
                this.gameService.playerMove(gameID, "addSettlement", settlementLocation, playerNo);
                // Deduct the resources used to build the settlement from the bot's resource cards
                consumeResourceCards("settlement", playerNo);
            }
        }

        // Check if the bot has enough resources to build a road
        if (hasEnoughResourcesForRoad(playerNo)) {
            // Find a suitable location to place a new road
            Integer roadLocation = this.gameboard.findAppropriateRoadPlacement(playerNo);

            // If a suitable location is found, build a road there
            if (roadLocation != null) {
                // Call the game service to add a road at the found location
                this.gameService.playerMove(gameID, "addRoad", roadLocation, playerNo);
                // Deduct the resources used to build the road from the bot's resource cards
                consumeResourceCards("road", playerNo);
            }
        }

        // Check if the bot has enough resources to upgrade a settlement to a city
        if (hasEnoughResourcesForUpgrade(playerNo)) {
            // Find a settlement that can be upgraded to a city
            Integer upgradeableSettlement = this.gameboard.findUpgradeableSettlement(playerNo);

            // If an upgradeable settlement is found, upgrade it to a city
            if (upgradeableSettlement != null) {
                // Call the game service to upgrade the settlement to a city
                this.gameService.playerMove(gameID, "upgradeSettlement", upgradeableSettlement, playerNo);
                // Deduct the resources used to upgrade the settlement from the bot's resource cards
                consumeResourceCards("upgrade", playerNo);
            }
        }
    }*/

    // end the turn and notify the players whose turn is started
    public Integer endTurn()
    {
        turn = turn%4 + 1;

        // set longest road
        // check winner
        // send log to the frontend
        // if there is a winner end the game and set the scores
        return turn;
        //todo
    }

    // once the dice is rolled add cards to card decks of players, depend on their settlement placements
    private void distributeAllCards(int currentDice)
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
        Card card = new Card();
        switch (tile.getTileType())
        {
            case FIELDS:
                card.setCardType(CardType.GRAIN);
                break;
            case FOREST:
                card.setCardType(CardType.LUMBER);
                break;
            case HILLS:
                card.setCardType(CardType.BRICK);
                break;
            case MOUNTAINS:
                card.setCardType(CardType.ORE);
                break;
            case PASTURES:
                card.setCardType(CardType.WOOL);
                break;
        }

        for (int i=0; i<gameboard.getSettlements().size(); i++)
        {
            if(isSettlementAdjacentToTile(gameboard.getSettlements().get(i).getNodeIndex(), tileIndex))
            {
                Settlement settlement = gameboard.getSettlements().get(i);
                int settlementOwnerPlayerNo = settlement.getPlayerNo();
                int cardIndex = playerCardDeckList.get(settlementOwnerPlayerNo - 1).getCards().indexOf(card);
                if(settlement.getSettlementLevel() == 1)
                {
                    playerCardDeckList.get(settlementOwnerPlayerNo - 1).getCards().get(cardIndex).incrementCardCount(1);
                }
                else
                {
                    playerCardDeckList.get(settlementOwnerPlayerNo - 1).getCards().get(cardIndex).incrementCardCount(2);
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
        // Define the resources needed for each build type
        Map<String, Map<CardType, Integer>> buildResources = new HashMap<>();
        buildResources.put("settlement", Map.of(CardType.LUMBER, 1, CardType.BRICK, 1, CardType.GRAIN, 1, CardType.WOOL, 1));
        buildResources.put("road", Map.of(CardType.LUMBER, 1, CardType.BRICK, 1));
        buildResources.put("upgrade", Map.of(CardType.ORE, 3, CardType.GRAIN, 2));

        // Get the resources needed for the specified build type
        Map<CardType, Integer> neededResources = buildResources.get(buildType);

        // For each needed resource, decrement its count
        for (Map.Entry<CardType, Integer> entry : neededResources.entrySet()) {
            Card temp = new Card();
            temp.setCardType(entry.getKey());
            int index = playerCardDeckList.get(playerNo - 1).getCards().indexOf(temp);
            playerCardDeckList.get(playerNo - 1).getCards().get(index).decrementCardCount(entry.getValue());
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
        currentLongestRoadLength = tempCurrentLongestRoadLength;
        currentLongestRoadOwnerPlayerNo = tempCurrentLongestRoadOwnerPlayerNo;
    }
}
