package CENG453.group18.entity;

import CENG453.group18.dictionary.GameBoardDictionary;
import CENG453.group18.enums.TileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "GAMEBOARD")
public class GameBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> settlements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> roads = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tile> tiles = new ArrayList<>();

    /* GameBoardDictionary is a universal and constant for all games,
     * no need to store it in a database      */

    @Transient
    private static final GameBoardDictionary gameBoardDictionary = new GameBoardDictionary();


    /*
    * Initialize the game board with random tile placements and random dice
    * number assignments.   */
    public GameBoard()
    {
        addTileTypes();
        addTileNumbers();
    }

    private void addTileTypes()
    {
        // Array to store how many tiles of each tile type to be placed on a map
        // index-0: # of hills, index-1: # of mountains, index-2: # of forests,
        // index-3: # of fields, index-4: # of pastures
        ArrayList<Integer> tileCounts = new ArrayList<Integer>(
                Arrays.asList(3,3,4,4,4));
        Random rand = new Random();
        int currentIndex;
        for(int i = 0; i < 19; i++)
        {
            // Add Desert
            if (i == 9)
            {
                Tile desertTile = new Tile();
                desertTile.setTileIndex(i);
                desertTile.setTileType(TileType.DESERT);
                tiles.add(desertTile);
                continue;
            }
            // pick a random index ranging 0 to 4
            currentIndex = rand.nextInt(5);
            // if the random tile is not available pick from the first available leftmost
            int j = 0;
            while (tileCounts.get(currentIndex)==0)
            {
                currentIndex = j++;
            }
            Tile tile = new Tile();
            tile.setTileIndex(i);
            tileCounts.set(currentIndex, tileCounts.get(currentIndex)-1);
            switch (currentIndex)
            {
                case 0:
                    tile.setTileType(TileType.HILLS);
                    break;
                case 1:
                    tile.setTileType(TileType.MOUNTAINS);
                    break;
                case 2:
                    tile.setTileType(TileType.FOREST);
                    break;
                case 3:
                    tile.setTileType(TileType.FIELDS);
                    break;
                case 4:
                    tile.setTileType(TileType.PASTURES);
                    break;
            }
            tiles.add(tile);
        }
    }
    private void addTileNumbers()
    {
        // Array to store how many numbers of each tileNumber to be placed on a map
        // index-0: # of 2s, index-1: # of 3s, index-2: # of 4s,
        // index-3: # of 5s, index-4: # of 6s, index-5: # of 7s and so on

        ArrayList<Integer> numberCounts = new ArrayList<Integer>(
                Arrays.asList(1,1,2,2,2,2,2,2,2,1,1));
        Random rand = new Random();
        int currentIndex;
        for(int i = 0; i < 19; i++)
        {
            // pass the desert
            if (i == 9)
            {
                continue;
            }
            // pick a random index ranging 0 to 10
            currentIndex = rand.nextInt(11);
            // if the random number is not available pick from the first available rightmost
            int j = 10;
            while (numberCounts.get(currentIndex)==0)
            {
                currentIndex = j--;
            }
            numberCounts.set(currentIndex, numberCounts.get(currentIndex)-1);
            tiles.get(i).setTileNumber(currentIndex + 2);
        }
    }

    // Check whether the node is appropriate for a player to place a settlement on this node
    public boolean isSettlementPlacementAppropriate(int nodeIndex, int playerNo)
    {


        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder("Settlements: [");
        for (Settlement settlement : settlements) {
            s.append(String.format("(nodeIndex: %d, playerNo: %d, settlementLevel: %d)\n", settlement.getNodeIndex(), settlement.getPlayerNo(), settlement.getSettlementLevel()));
        }
        s.append("]\nRoads: [");
        for (Road road: roads) {
            s.append(String.format("(edgeIndex: %d, playerNo: %d)\n", road.getEdgeIndex(), road.getPlayerNo()));
        }
        s.append("]\nTiles: [");
        for (Tile tile: tiles) {
            s.append(String.format("(tileIndex: %d, tileType: %s, tileNumber: %d)\n", tile.getTileIndex(), tile.getTileType(), tile.getTileNumber()));
        }
        s.append("]\n");
        return s.toString();
    }
}
