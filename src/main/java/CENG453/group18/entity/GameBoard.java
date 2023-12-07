package CENG453.group18.entity;

import CENG453.group18.dictionary.EdgeDictionaryObject;
import CENG453.group18.dictionary.GameBoardDictionary;
import CENG453.group18.dictionary.NodeDictionaryObject;
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

    // once the gameboard is deleted delete all game components from the database
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

    // Stores all node and their adjacent nodes' indices
    // For any new settlement these indices are not valid to place
    @ElementCollection
    @CollectionTable(name = "FORBIDDEN_NODE_INDICES", joinColumns = @JoinColumn(name = "gameboard_id"))
    @Column(name = "node_index")
    private Set<Integer> forbiddenNodeIndices = new HashSet<>();
    /*
    * Initialize the game board with random tile placements and random dice
    * number assignments.   */
    public GameBoard()
    {
        forbiddenNodeIndices = new HashSet<>();
        addTileTypes();
        addTileNumbers();
        addInitialSettlements();
        addInitialRoads();
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

    private void addInitialSettlements()
    {
        Random rand = new Random();
        for(int i=0; i<4; i++)
        {
            int nodeIndex = rand.nextInt(54);
            while(forbiddenNodeIndices.contains(nodeIndex))
            {
                if (nodeIndex < 53)
                {
                    nodeIndex++;
                }
                else
                {
                    nodeIndex = 0;
                }
            }
            forbiddenNodeIndices.add(nodeIndex);
            forbiddenNodeIndices.addAll(gameBoardDictionary.getNode(nodeIndex).getAdjacentNodes());
            Settlement settlement = new Settlement();
            settlement.setSettlementLevel(1);
            settlement.setNodeIndex(nodeIndex);
            settlement.setPlayerNo(i+1);
            settlements.add(settlement);
        }
    }
    private void addInitialRoads()
    {
        Random rand = new Random();
        int i=1;
        for(Settlement settlement: settlements)
        {
            NodeDictionaryObject nodeDictionaryObject = gameBoardDictionary.getNode(settlement.getNodeIndex());
            Road road = new Road();
            road.setPlayerNo(i);
            int edgeIndex = nodeDictionaryObject.getAdjacentEdges().get(rand.nextInt(nodeDictionaryObject.getAdjacentEdges().size()));
            road.setEdgeIndex(edgeIndex);
            i++;
            roads.add(road);
        }
    }

    // Check whether the node is appropriate for a player to place a settlement on
    public boolean isSettlementPlacementAppropriate(int nodeIndex, int playerNo)
    {
        NodeDictionaryObject node = gameBoardDictionary.getNode(nodeIndex);

        // check whether the settlement is between at least 2 roads belong to the same player who calls this function
        int adjacentRoadCount = 0;
        for (int i=0; i<node.getAdjacentEdges().size(); i++)
        {
            Road temp = new Road();
            temp.setEdgeIndex(node.getAdjacentEdges().get(i));
            temp.setPlayerNo(playerNo);
            if(roads.contains(temp))
            {
                adjacentRoadCount++;
            }
        }
        if(adjacentRoadCount<2)
        {
            return false;
        }

        // check whether there exists any settlement in the node and adjacent nodes
        if(forbiddenNodeIndices.contains(nodeIndex))
        {
            return false;
        }
        return true;
    }
    // Check whether the edge is appropriate for a player to place a road on
    public boolean isRoadPlacementAppropriate(int edgeIndex, int playerNo)
    {
        EdgeDictionaryObject edgeDictionaryObject = gameBoardDictionary.getEdge(edgeIndex);

        // check whether there exists a road already placed in an edge with edgeIndex
        for (Road road : roads) {
            if (road.getEdgeIndex() == edgeIndex) {
                return false;
            }
        }
        // check whether there exists a settlement which belongs to the player adjacent to the edge with edgeIndex
        // if not found, check for adjacent edges to check if any road belongs to the same player exists
        Integer node1_index = edgeDictionaryObject.getNode1_index();
        Integer node2_index = edgeDictionaryObject.getNode2_index();

        Settlement temp1 = new Settlement();
        Settlement temp2 = new Settlement();

        temp1.setNodeIndex(node1_index);
        temp2.setNodeIndex(node2_index);

        temp1.setPlayerNo(playerNo);
        temp2.setPlayerNo(playerNo);

        if(settlements.contains(temp1) || settlements.contains(temp2))
        {
            return true;
        }

        // not found, check adjacent road exists
        NodeDictionaryObject nodeDictionaryObject1 = gameBoardDictionary.getNode(node1_index);
        for (int i=0; i<nodeDictionaryObject1.getAdjacentEdges().size(); i++)
        {
            Road temp = new Road();
            temp.setEdgeIndex(nodeDictionaryObject1.getAdjacentEdges().get(i));
            temp.setPlayerNo(playerNo);
            if(roads.contains(temp))
            {
                return true;
            }
        }
        NodeDictionaryObject nodeDictionaryObject2 = gameBoardDictionary.getNode(node2_index);
        for (int i=0; i<nodeDictionaryObject2.getAdjacentEdges().size(); i++)
        {
            Road temp = new Road();
            temp.setEdgeIndex(nodeDictionaryObject2.getAdjacentEdges().get(i));
            temp.setPlayerNo(playerNo);
            if(roads.contains(temp))
            {
                return true;
            }
        }
        return false;
    }

    public Settlement addSettlement(int nodeIndex, int playerNo)
    {
        if(isSettlementPlacementAppropriate(nodeIndex, playerNo))
        {
            forbiddenNodeIndices.add(nodeIndex);
            forbiddenNodeIndices.addAll(gameBoardDictionary.getNode(nodeIndex).getAdjacentNodes());

            Settlement settlement = new Settlement();
            settlement.setNodeIndex(nodeIndex);
            settlement.setPlayerNo(playerNo);
            settlement.setSettlementLevel(1);
            settlements.add(settlement);
            return settlement;
        }
        return null;
    }
    public Road addRoad(int edgeIndex, int playerNo)
    {
        if(isRoadPlacementAppropriate(edgeIndex, playerNo))
        {
            Road road = new Road();
            road.setEdgeIndex(edgeIndex);
            road.setPlayerNo(playerNo);
            roads.add(road);
            return road;
        }
        return null;
    }
    public Settlement upgradeSettlement(int nodeIndex, int playerNo)
    {
        // check whether settlement belongs to the player and whether it is already upgraded
        Settlement temp = new Settlement();
        temp.setNodeIndex(nodeIndex);
        temp.setPlayerNo(playerNo);
        temp.setSettlementLevel(2);
        int index = settlements.indexOf(temp);
        if (index!=-1 && settlements.get(index).getSettlementLevel() == 1)
        {
            settlements.get(index).setSettlementLevel(2);
            return  settlements.get(index);
        }
        return null;
    }
    // find the longest path belongs to the player
    public Integer findLongestRoadLengthOfPlayer(int playerNo)
    {
        // todo
        return 0;
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
