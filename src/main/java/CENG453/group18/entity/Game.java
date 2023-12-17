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
import org.antlr.v4.runtime.misc.Pair;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    // Constructor must be changed by adding and attaching players and additional features
    public Game(int playerID) {
        this.gameboard = new GameBoard();
        turn = 1;
        if(gameType == GameType.SinglePlayer)
        {
            playerIDs[0] = playerID;
        }
        for(int i=0; i<4; i++)
        {
            PlayerCardDeck playerCardDeck = new PlayerCardDeck();
            ArrayList<Card> cards = new ArrayList<>();
            cards.add(new Card(CardType.GRAIN));
            cards.add(new Card(CardType.WOOL));
            cards.add(new Card(CardType.ORE));
            cards.add(new Card(CardType.BRICK));
            cards.add(new Card(CardType.LUMBER));
            playerCardDeck.setCards(cards);
            playerCardDeckList.add(playerCardDeck);
        }

        // add cards to each player at beginning (resources adjacent to initial settlements)
        // implement distributeCards and add here
        for(int i=2; i<13; i++)
        {
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

    // bot with playerNo plays its turn
    // human player does not need this automated function. If it is human player's turn
    // then depending on player's choice call api functions separately until endTurn is invoked by the
    // player
    public void botPlay(int playerNo)
    {

        // don't call endTurn or rollTheDice here, they are already called in the botPlay service
        // only implement bot logic here using resource cards, game board and the dice
        // can add sleep function to make it look like a human
        System.out.println(currentDice);

        /**
        // Check if the bot has enough resources to build a settlement
        if (hasEnoughResourcesForSettlement(playerNo)) {
            // Find an appropriate location for the settlement
            Integer settlementLocation = this.gameboard.findAppropriateSettlementPlacement(playerNo);

            // If a suitable location is found...
            if (settlementLocation != null) {
                // Add a settlement there
                this.gameboard.addSettlementToPlayer(new Settlement(settlementLocation), playerNo);

                // Deduct the resources used to build the settlement
                deductResourcesForSettlement(playerNo);
            }
        }

        // Check if the bot has enough resources to build a road
        if (hasEnoughResourcesForRoad(playerNo)) {
            // Find an appropriate location for the road
            Integer roadLocation = this.gameboard.findAppropriateRoadPlacement(playerNo);

            // If a suitable location is found...
            if (roadLocation != null) {
                // Add a road there
                this.gameboard.addRoadToPlayer(new Road(roadLocation), playerNo);

                // Deduct the resources used to build the road
                deductResourcesForRoad(playerNo);
            }
        }
        **/




        //todo

    }

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
        Tile tile = new Tile();
        tile.setTileIndex(tileIndex);
        return nodeDictionaryObject.getAdjacentTiles().contains(tile);
    }

    public boolean areThereEnoughResources(String buildType, int playerNo)
    {
        List<Card> cards = getPlayerCardDeckList().get(playerNo -1).getCards();
        int trueCount = 0;
        switch (buildType)
        {
            case "settlement":
                for(int i=0; i<cards.size(); i++)
                {
                    if(cards.get(i).getCardType() == CardType.LUMBER && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                    else if(cards.get(i).getCardType() == CardType.BRICK && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                    else if(cards.get(i).getCardType() == CardType.GRAIN && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                    else if(cards.get(i).getCardType() == CardType.WOOL && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                }
                if(trueCount == 4)
                {
                    return true;
                }
                break;

            case "road":
                for(int i=0; i<cards.size(); i++)
                {
                    if(cards.get(i).getCardType() == CardType.LUMBER && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                    else if(cards.get(i).getCardType() == CardType.BRICK && cards.get(i).getCardCount() >= 1)
                    {
                        trueCount ++;
                    }
                }
                if(trueCount == 2)
                {
                    return true;
                }
                break;
            case "upgrade":
                for(int i=0; i<cards.size(); i++)
                {
                    if(cards.get(i).getCardType() == CardType.ORE && cards.get(i).getCardCount() >= 3)
                    {
                        trueCount ++;
                    }
                    else if(cards.get(i).getCardType() == CardType.GRAIN && cards.get(i).getCardCount() >= 2)
                    {
                        trueCount ++;
                    }
                }
                if(trueCount == 2)
                {
                    return true;
                }
                break;
        }
        return false;
    }
    public void consumeResourceCards(String buildType, int playerNo)
    {
        switch (buildType)
        {
            case "settlement":
                Card temp1 = new Card();
                temp1.setCardType(CardType.LUMBER);
                int index1 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp1);
                playerCardDeckList.get(playerNo-1).getCards().get(index1).decrementCardCount(1);

                Card temp2 = new Card();
                temp2.setCardType(CardType.BRICK);
                int index2 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp2);
                playerCardDeckList.get(playerNo-1).getCards().get(index2).decrementCardCount(1);

                Card temp3 = new Card();
                temp3.setCardType(CardType.GRAIN);
                int index3 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp3);
                playerCardDeckList.get(playerNo-1).getCards().get(index3).decrementCardCount(1);

                Card temp4 = new Card();
                temp4.setCardType(CardType.WOOL);
                int index4 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp4);
                playerCardDeckList.get(playerNo-1).getCards().get(index4).decrementCardCount(1);
                break;
            case "road":
                Card temp5 = new Card();
                temp5.setCardType(CardType.LUMBER);
                int index5 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp5);
                playerCardDeckList.get(playerNo-1).getCards().get(index5).decrementCardCount(1);

                Card temp6 = new Card();
                temp6.setCardType(CardType.BRICK);
                int index6 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp6);
                playerCardDeckList.get(playerNo-1).getCards().get(index6).decrementCardCount(1);
                break;
            case "upgrade":
                Card temp7 = new Card();
                temp7.setCardType(CardType.ORE);
                int index7 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp7);
                playerCardDeckList.get(playerNo-1).getCards().get(index7).decrementCardCount(3);

                Card temp8 = new Card();
                temp8.setCardType(CardType.GRAIN);
                int index8 = playerCardDeckList.get(playerNo-1).getCards().indexOf(temp8);
                playerCardDeckList.get(playerNo-1).getCards().get(index8).decrementCardCount(2);
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
        currentLongestRoadLength = tempCurrentLongestRoadLength;
        currentLongestRoadOwnerPlayerNo = tempCurrentLongestRoadOwnerPlayerNo;
    }
}
