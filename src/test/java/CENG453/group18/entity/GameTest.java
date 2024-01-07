package CENG453.group18.entity;

import CENG453.group18.dictionary.GameBoardDictionary;
import CENG453.group18.enums.CardType;
import CENG453.group18.enums.GameType;
import CENG453.group18.enums.TileType;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Player player1;
    private Game game;
    @BeforeEach
    void init()
    {
        player1 = new Player(1,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "");
        game = new Game(player1, null, null, null, GameType.SinglePlayer);
    }
    @Test
    @Order(2)
    void testCardDistribution()
    {
        GameBoardDictionary gameBoardDictionary = new GameBoardDictionary();
        // get the first settlement in the list and store its data in the local variables below
        Settlement settlement = game.getGameboard().getSettlements().get(0);
        int nodeIndex = settlement.getNodeIndex();
        int ownerPlayerNo = settlement.getPlayerNo();
        // find its adjacent tiles
        List<Integer> adjacentTiles = gameBoardDictionary.getNode(nodeIndex).getAdjacentTiles();
        // find owner's card counts
        Map<CardType,Integer> playerResources = game.getPlayerCardDeckList().get(ownerPlayerNo-1).getResourceCounts();
        // distribute cards to the player in his/her adjacent nodes
        // for simplicity the tile types will be set Pastures in this iteration
        // distribute number of adjacent tiles times WOOL
        for (Integer adjacentTile : adjacentTiles) {
            game.getGameboard().getTiles().get(adjacentTile).setTileType(TileType.PASTURES);
            game.distributeAllCards(game.getGameboard().getTiles().get(adjacentTile).getTileNumber());
        }
        // check if the desired increment is done
        assertEquals(playerResources.get(CardType.WOOL) + adjacentTiles.size(), game.getPlayerCardDeckList().get(ownerPlayerNo-1).getResourceCounts().get(CardType.WOOL));

        // set settlement level to two and distribute it again to check resource harvest is doubled
        game.getGameboard().getSettlements().get(0).setSettlementLevel(2);
        for (Integer adjacentTile : adjacentTiles) {
            game.getGameboard().getTiles().get(adjacentTile).setTileType(TileType.PASTURES);
            game.distributeAllCards(game.getGameboard().getTiles().get(adjacentTile).getTileNumber());
        }
        // check if the desired increment is done
        // 3 times increment : 1 comes from the previous harvest, 2 comes from the current harvest
        assertEquals(playerResources.get(CardType.WOOL) +3 * adjacentTiles.size(), game.getPlayerCardDeckList().get(ownerPlayerNo-1).getResourceCounts().get(CardType.WOOL));
    }
    @Test
    @Order(2)
    void testEnoughResourceForBuilding()
    {
        Card card1 = new Card(5500, CardType.GRAIN, 0);
        Card card2 = new Card(5501, CardType.WOOL, 0);
        Card card3 = new Card(5502, CardType.LUMBER, 0);
        Card card4 = new Card(5503, CardType.BRICK, 0);
        Card card5 = new Card(5504, CardType.ORE, 0);

        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(card1,card2,card3,card4,card5));
        game.getPlayerCardDeckList().get(0).setCards(cards);

        // initially player-1 will not be able to build any kind of building
        assertFalse(game.hasEnoughResourcesForRoad(1));
        assertFalse(game.hasEnoughResourcesForSettlement(1));
        assertFalse(game.hasEnoughResourcesForUpgrade(1));

        // add enough resources for building a road
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.LUMBER, 1);
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.BRICK, 1);
        assertTrue(game.hasEnoughResourcesForRoad(1));
        game.consumeResourceCards("road", 1);
        // since the resources of the player consumed it will not be able to build another road
        assertFalse(game.hasEnoughResourcesForRoad(1));
        assertFalse(game.hasEnoughResourcesForSettlement(1));
        assertFalse(game.hasEnoughResourcesForUpgrade(1));

        // add enough resources for building a settlement
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.LUMBER, 1);
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.BRICK, 1);
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.GRAIN, 1);
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.WOOL, 1);
        assertTrue(game.hasEnoughResourcesForSettlement(1));
        game.consumeResourceCards("settlement", 1);
        // since the resources of the player consumed it will not be able to build another settlement
        assertFalse(game.hasEnoughResourcesForRoad(1));
        assertFalse(game.hasEnoughResourcesForSettlement(1));
        assertFalse(game.hasEnoughResourcesForUpgrade(1));

        // add enough resources for settlement upgrade
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.ORE, 3);
        game.getPlayerCardDeckList().get(0).incrementResourceCounts(CardType.GRAIN, 2);
        assertTrue(game.hasEnoughResourcesForUpgrade(1));
        game.consumeResourceCards("upgrade", 1);
        // since the resources of the player consumed it will not be able to upgrade another settlement
        assertFalse(game.hasEnoughResourcesForRoad(1));
        assertFalse(game.hasEnoughResourcesForSettlement(1));
        assertFalse(game.hasEnoughResourcesForUpgrade(1));
    }



}
