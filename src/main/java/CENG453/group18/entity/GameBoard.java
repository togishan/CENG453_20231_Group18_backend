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
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "GAMEBOARD")
public class GameBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Game components that are removed from the database when the gameboard is deleted
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> settlements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> roads = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tile> tiles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> player1Roads = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> player2Roads = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> player3Roads = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Road> player4Roads = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> player1Settlements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> player2Settlements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> player3Settlements = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Settlement> player4Settlements = new ArrayList<>();

    // Universal and constant GameBoardDictionary for all games
    @Transient
    public static final GameBoardDictionary gameBoardDictionary = new GameBoardDictionary();

    // Set to store all node and their adjacent nodes' indices
    @ElementCollection
    @CollectionTable(name = "FORBIDDEN_NODE_INDICES", joinColumns = @JoinColumn(name = "gameboard_id"))
    @Column(name = "node_index")
    private Set<Integer> forbiddenNodeIndices = new HashSet<>();

    // Constructor to initialize the game board with random tile placements and random dice number assignments
    public GameBoard() {
        forbiddenNodeIndices = new HashSet<>();
        addTileTypes();
        addTileNumbers();
        addInitialSettlements();
        addInitialRoads();
    }

    /**
     * This method finds an appropriate placement for a road for a given player.
     *
     * @param playerNo The number of the player (1-4).
     * @return The index of an appropriate edge for road placement, or null if no such edge exists.
     */
    public Integer findAppropriateRoadPlacement(int playerNo) {
        // Get a list of all edges where the player can place a road
        List<Integer> appropriateEdges = findAllAppropriateEdges(playerNo);

        // If there are no appropriate edges, return null
        if (appropriateEdges.isEmpty()) {
            return null;
        }

        // Otherwise, randomly select one of the appropriate edges and return its index
        Random rand = new Random();
        int randomIndex = rand.nextInt(appropriateEdges.size());
        return appropriateEdges.get(randomIndex);
    }

    /**
     * This helper method finds all edges where a given player can place a road.
     *
     * @param playerNo The number of the player (1-4).
     * @return A list of the indices of all appropriate edges for road placement.
     */
    private List<Integer> findAllAppropriateEdges(int playerNo) {
        // Create a list to store the indices of the appropriate edges
        List<Integer> appropriateEdges = new ArrayList<>();

        // Get a list of the player's roads
        List<Road> playerRoads = getPlayerRoads(playerNo);

        // For each of the player's roads...
        for (Road road : playerRoads) {
            // Find the indices of the edges adjacent to the road
            List<Integer> adjacentEdgeIndices = findAdjacentEdgeIndices(road.getEdgeIndex()); 

            // For each of these adjacent edges...
            for (Integer edgeIndex : adjacentEdgeIndices) {
                // If the player can place a road on the edge...
                if (isRoadPlacementAppropriate(edgeIndex, playerNo)) { 
                    // Add the edge's index to the list of appropriate edges
                    appropriateEdges.add(edgeIndex);
                }
            }
        }

        // Return the list of appropriate edges
        return appropriateEdges;
    }

    public List<Integer> findAdjacentEdgeIndices(int edgeIndex) {
        List<Integer> adjacentEdgeIndices = new ArrayList<>();
        EdgeDictionaryObject edgeDictionaryObject = gameBoardDictionary.getEdge(edgeIndex);
        
        // Get the indices of the nodes at the ends of the edge
        Integer node1_index = edgeDictionaryObject.getNode1_index();
        Integer node2_index = edgeDictionaryObject.getNode2_index();
        
        // Get the adjacent edges for each node
        List<Integer> adjacentEdgeIndices1 = gameBoardDictionary.getNode(node1_index).getAdjacentEdges();
        List<Integer> adjacentEdgeIndices2 = gameBoardDictionary.getNode(node2_index).getAdjacentEdges();
        
        // Combine the lists of adjacent edges
        adjacentEdgeIndices.addAll(adjacentEdgeIndices1);
        adjacentEdgeIndices.addAll(adjacentEdgeIndices2);
        
        return adjacentEdgeIndices;
    }

    public boolean isRoadPlacementAppropriate(int edgeIndex, int playerNo) {
        // Check if there's already a road on the given edge
        if (isRoadAlreadyPlaced(edgeIndex)) {
            return false;
        }

        // Check if there's a road belonging to the player on an edge adjacent to the given edge
        if (isAdjacentRoadBelongsToPlayer(edgeIndex, playerNo)) {
            return true;
        }

        // If none of the above conditions are met, it's not appropriate to place a road on the given edge
        return false;
    }

    private boolean isRoadAlreadyPlaced(int edgeIndex) {
        for (Road road : roads) {
            if (road.getEdgeIndex() == edgeIndex) {
                return true;
            }
        }
        return false;
    }

    private boolean isAdjacentRoadBelongsToPlayer(int edgeIndex, int playerNo) {
        List<Integer> adjacentEdgeIndices = findAdjacentEdgeIndices(edgeIndex);
        for (Integer adjacentEdgeIndex : adjacentEdgeIndices) {
            for (Road road : roads) {
                if (road.getEdgeIndex() == adjacentEdgeIndex && road.getPlayerNo() == playerNo) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method finds an appropriate placement for a settlement for a given player.
     *
     * @param playerNo The number of the player (1-4).
     * @return The index of an appropriate node for settlement placement, or null if no such node exists.
     */
    public Integer findAppropriateSettlementPlacement(int playerNo) {
        // Get a list of all nodes where the player can place a settlement
        List<Integer> appropriateNodes = findAllAppropriateNodes(playerNo);

        // If there are no appropriate nodes, return null
        if (appropriateNodes.isEmpty()) {
            return null;
        }

        // Otherwise, randomly select one of the appropriate nodes and return its index
        Random rand = new Random();
        int randomIndex = rand.nextInt(appropriateNodes.size());
        return appropriateNodes.get(randomIndex);
    }

    /**
     * This helper method finds all nodes where a given player can place a settlement.
     *
     * @param playerNo The number of the player (1-4).
     * @return A list of the indices of all appropriate nodes for settlement placement.
     */
    private List<Integer> findAllAppropriateNodes(int playerNo) {
        // Create a list to store the indices of the appropriate nodes
        List<Integer> appropriateNodes = new ArrayList<>();

        // Get a list of the player's nodes
        List<Integer> playerNodes = getPlayerNodes(playerNo);

        // For each of these nodes...
        for (Integer nodeIndex : playerNodes) {
            // If the player can place a settlement on the node...
            if (isSettlementPlacementAppropriate(nodeIndex, playerNo)) { 
                // Add the node's index to the list of appropriate nodes
                appropriateNodes.add(nodeIndex);
            }
        }

        // Return the list of appropriate nodes
        return appropriateNodes;
    }

    /**
     * This helper method gets a list of nodes that a given player has.
     *
     * @param playerNo The number of the player (1-4).
     * @return A list of the indices of all nodes that the player has.
     */
    private List<Integer> getPlayerNodes(int playerNo) {
        // Create a list to store the indices of the player's nodes
        List<Integer> playerNodes = new ArrayList<>();

        // Get a list of the player's roads
        List<Road> playerRoads = getPlayerRoads(playerNo);

        // For each of the player's roads...
        for (Road road : playerRoads) {
            // Get the indices of the nodes at the ends of the road
            Integer node1_index = gameBoardDictionary.getEdge(road.getEdgeIndex()).getNode1_index();
            Integer node2_index = gameBoardDictionary.getEdge(road.getEdgeIndex()).getNode2_index();

            // Add the node indices to the list of player's nodes
            playerNodes.add(node1_index);
            playerNodes.add(node2_index);
        }

        // Remove duplicates from the list
        playerNodes = new ArrayList<>(new HashSet<>(playerNodes));

        // Return the list of player's nodes
        return playerNodes;
    }

    /**
     * This method adds tile types to the game board.
     * It randomly assigns a type to each tile, ensuring that the correct number of each type is used.
     */
    private void addTileTypes() {
        // Array to store how many tiles of each tile type to be placed on a map
        // index-0: # of hills, index-1: # of mountains, index-2: # of forests,
        // index-3: # of fields, index-4: # of pastures
        List<Integer> tileCounts = new ArrayList<>(Arrays.asList(3, 3, 4, 4, 4));
        Random rand = new Random();

        // Iterate over each tile on the board
        for (int i = 0; i < 19; i++) {
            // Create a new tile
            Tile tile = new Tile();
            tile.setTileIndex(i);

            // If this is the 10th tile, make it a desert
            if (i == 9) {
                tile.setTileType(TileType.DESERT);
            } else {
                // Otherwise, assign a random type to the tile
                int tileTypeIndex = getRandomTileTypeIndex(tileCounts, rand);
                tileCounts.set(tileTypeIndex, tileCounts.get(tileTypeIndex) - 1);
                tile.setTileType(TileType.values()[tileTypeIndex]);
            }

            // Add the tile to the list of tiles
            tiles.add(tile);
        }
    }

    /**
     * This helper method gets a random index for a tile type that is still available.
     *
     * @param tileCounts The list of counts for each tile type.
     * @param rand A Random object for generating random numbers.
     * @return The index of a tile type that is still available.
     */
    private int getRandomTileTypeIndex(List<Integer> tileCounts, Random rand) {
        int tileTypeIndex = rand.nextInt(5);
        while (tileCounts.get(tileTypeIndex) == 0) {
            tileTypeIndex = (tileTypeIndex + 1) % 5;
        }
        return tileTypeIndex;
    }

    /**
     * This method adds tile numbers to the game board.
     * It randomly assigns a number to each tile, ensuring that the correct number of each tile number is used.
     */
    private void addTileNumbers() {
        // Array to store how many numbers of each tileNumber to be placed on a map
        // index-0: # of 2s, index-1: # of 3s, index-2: # of 4s,
        // index-3: # of 5s, index-4: # of 6s, index-5: # of 7s and so on
        List<Integer> numberCounts = new ArrayList<>(Arrays.asList(1,2,2,2,2,0,2,2,2,2,1));
        Random rand = new Random();

        for(int i = 0; i < 19; i++) {
            // Skip the desert tile
            if (i == 9) {
                continue;
            }

            // Get a random index for a tile number that is still available
            int tileNumberIndex = getRandomTileNumberIndex(numberCounts, rand);

            // Decrease the count of the selected tile number
            numberCounts.set(tileNumberIndex, numberCounts.get(tileNumberIndex) - 1);

            // Assign the selected tile number to the tile
            tiles.get(i).setTileNumber(tileNumberIndex + 2);
        }
    }

    /**
     * This helper method gets a random index for a tile number that is still available.
     *
     * @param numberCounts The list of counts for each tile number.
     * @param rand A Random object for generating random numbers.
     * @return The index of a tile number that is still available.
     */
    private int getRandomTileNumberIndex(List<Integer> numberCounts, Random rand) {
        int tileNumberIndex = rand.nextInt(11);
        while (numberCounts.get(tileNumberIndex) == 0) {
            tileNumberIndex = (tileNumberIndex + 1) % 11;
        }
        return tileNumberIndex;
    }

    /**
     * This method adds initial settlements for each player on the game board.
     * It randomly assigns a node to each settlement, ensuring that no two settlements are adjacent.
     */
    private void addInitialSettlements() {
        Random rand = new Random();

        // Iterate over each player
        for(int i = 0; i < 4; i++) {
            // Get a random node index that is not forbidden
            int nodeIndex = getRandomNodeIndex(rand);

            // Add the node index to the list of forbidden indices
            forbiddenNodeIndices.add(nodeIndex);

            // Add the indices of the adjacent nodes to the list of forbidden indices
            forbiddenNodeIndices.addAll(gameBoardDictionary.getNode(nodeIndex).getAdjacentNodes());

            // Create a new settlement and set its properties
            Settlement settlement = new Settlement();
            settlement.setSettlementLevel(1);
            settlement.setNodeIndex(nodeIndex);
            settlement.setPlayerNo(i + 1);

            // Add the settlement to the list of all settlements
            settlements.add(settlement);

            // Add the settlement to the list of settlements for the current player
            addSettlementToPlayer(settlement, i + 1);
        }
    }

    /**
     * This helper method gets a random node index that is not in the list of forbidden indices.
     *
     * @param rand A Random object for generating random numbers.
     * @return A random node index that is not in the list of forbidden indices.
     */
    private int getRandomNodeIndex(Random rand) {
        int nodeIndex = rand.nextInt(54);
        while(forbiddenNodeIndices.contains(nodeIndex)) {
            nodeIndex = (nodeIndex + 1) % 54;
        }
        return nodeIndex;
    }

    /**
     * This helper method adds a settlement to the list of settlements for a given player.
     *
     * @param settlement The settlement to add.
     * @param playerNo The number of the player (1-4).
     */
    private void addSettlementToPlayer(Settlement settlement, int playerNo) {
        switch (playerNo) {
            case 1:
                player1Settlements.add(settlement);
                break;
            case 2:
                player2Settlements.add(settlement);
                break;
            case 3:
                player3Settlements.add(settlement);
                break;
            case 4:
                player4Settlements.add(settlement);
                break;
            default:
                // If the player number is not valid, do nothing
                break;
        }
    }

    /**
     * This method adds initial roads for each player on the game board.
     * It randomly assigns an edge to each road, ensuring that the road is adjacent to one of the player's settlements.
     */
    private void addInitialRoads() {
        Random rand = new Random();

        // Iterate over each settlement
        for(int i = 0; i < settlements.size(); i++) {
            Settlement settlement = settlements.get(i);

            // Get the NodeDictionaryObject for the node where the settlement is located
            NodeDictionaryObject nodeDictionaryObject = gameBoardDictionary.getNode(settlement.getNodeIndex());

            // Create a new road and set its properties
            Road road = new Road();
            road.setPlayerNo(i + 1);
            int edgeIndex = getRandomEdgeIndex(nodeDictionaryObject, rand);
            road.setEdgeIndex(edgeIndex);

            // Add the road to the list of all roads
            roads.add(road);

            // Add the road to the list of roads for the current player
            addRoadToPlayer(road, i + 1);
        }
    }

    /**
     * This helper method gets a random edge index that is adjacent to a given node.
     *
     * @param nodeDictionaryObject The NodeDictionaryObject for the node.
     * @param rand A Random object for generating random numbers.
     * @return A random edge index that is adjacent to the node.
     */
    private int getRandomEdgeIndex(NodeDictionaryObject nodeDictionaryObject, Random rand) {
        return nodeDictionaryObject.getAdjacentEdges().get(rand.nextInt(nodeDictionaryObject.getAdjacentEdges().size()));
    }

    /**
     * This helper method adds a road to the list of roads for a given player.
     *
     * @param road The road to add.
     * @param playerNo The number of the player (1-4).
     */
    private void addRoadToPlayer(Road road, int playerNo) {
        switch (playerNo) {
            case 1:
                player1Roads.add(road);
                break;
            case 2:
                player2Roads.add(road);
                break;
            case 3:
                player3Roads.add(road);
                break;
            case 4:
                player4Roads.add(road);
                break;
            default:
                // If the player number is not valid, do nothing
                break;
        }
    }

    /**
     * This method checks whether a given node is appropriate for a player to place a settlement on.
     *
     * @param nodeIndex The index of the node.
     * @param playerNo The number of the player (1-4).
     * @return True if the node is appropriate for the player to place a settlement on, false otherwise.
     */
    public boolean isSettlementPlacementAppropriate(int nodeIndex, int playerNo) {
        NodeDictionaryObject node = gameBoardDictionary.getNode(nodeIndex);

        // Check whether the settlement is adjacent to at least one road belong to the same player
        if (!hasOneAdjacentRoad(node, playerNo)) {
            return false;
        }

        // Check whether there exists any settlement in the node and adjacent nodes
        if (forbiddenNodeIndices.contains(nodeIndex)) {
            return false;
        }

        return true;
    }

    /**
     * This method checks if a given node has at least one adjacent road that belongs to a specific player.
     *
     * @param node The NodeDictionaryObject representing the node to check.
     * @param playerNo The number of the player (1-4).
     * @return True if the node has at least one adjacent road that belongs to the player, false otherwise.
     */
    private boolean hasOneAdjacentRoad(NodeDictionaryObject node, int playerNo) {
        // Iterate over each edge adjacent to the node
        for (int edgeIndex : node.getAdjacentEdges()) {
            // Create a temporary Road object with the current edge index and player number
            Road temp = new Road();
            temp.setEdgeIndex(edgeIndex);
            temp.setPlayerNo(playerNo);

            // If the list of roads contains the temporary road, return true
            if (roads.contains(temp)) {
                return true;
            }
        }

        // If no adjacent road belonging to the player was found, return false
        return false;
    }


    /**
     * This method adds a settlement for a player at a given node on the game board.
     *
     * @param nodeIndex The index of the node.
     * @param playerNo The number of the player (1-4).
     * @return The new Settlement object if the settlement was added successfully, null otherwise.
     */
    public Settlement addSettlement(int nodeIndex, int playerNo) {
        // Check if the settlement placement is appropriate
        if (!isSettlementPlacementAppropriate(nodeIndex, playerNo)) {
            return null;
        }

        // Add the node index to the list of forbidden node indices
        addForbiddenNodeIndices(nodeIndex);

        // Create a new settlement and set its properties
        Settlement settlement = createSettlement(nodeIndex, playerNo);

        // Add the settlement to the list of settlements for the current player
        addSettlementToPlayer(settlement, playerNo);

        return settlement;
    }

    // This helper method adds a node index and its adjacent node indices to the list of forbidden node indices.
    private void addForbiddenNodeIndices(int nodeIndex) {
        forbiddenNodeIndices.add(nodeIndex);
        forbiddenNodeIndices.addAll(gameBoardDictionary.getNode(nodeIndex).getAdjacentNodes());
    }

    // This helper method creates a new settlement and sets its properties.
    private Settlement createSettlement(int nodeIndex, int playerNo) {
        Settlement settlement = new Settlement();
        settlement.setNodeIndex(nodeIndex);
        settlement.setPlayerNo(playerNo);
        settlement.setSettlementLevel(1);
        settlements.add(settlement);
        return settlement;
    }

    /**
     * This method adds a road for a player at a given edge on the game board.
     *
     * @param edgeIndex The index of the edge.
     * @param playerNo The number of the player (1-4).
     * @return The new Road object if the road was added successfully, null otherwise.
     */
    public Road addRoad(int edgeIndex, int playerNo) {
        // Check if the road placement is appropriate
        if (!isRoadPlacementAppropriate(edgeIndex, playerNo)) {
            return null;
        }

        // Create a new road and set its properties
        Road road = createRoad(edgeIndex, playerNo);

        // Add the road to the list of roads for the current player
        addRoadToPlayer(road, playerNo);

        return road;
    }

    // This helper method creates a new road and sets its properties.
    private Road createRoad(int edgeIndex, int playerNo) {
        Road road = new Road();
        road.setEdgeIndex(edgeIndex);
        road.setPlayerNo(playerNo);
        roads.add(road);
        return road;
    }

    /**
     * This method upgrades a settlement for a player at a given node on the game board.
     *
     * @param nodeIndex The index of the node.
     * @param playerNo The number of the player (1-4).
     * @return The upgraded Settlement object if the settlement was upgraded successfully, null otherwise.
     */
    public Settlement upgradeSettlement(int nodeIndex, int playerNo) {
        // Find the settlement belonging to the player at the given node
        Settlement settlement = findPlayerSettlement(nodeIndex, playerNo);

        // If the settlement exists and is not already upgraded, upgrade it
        if (settlement != null && settlement.getSettlementLevel() == 1) {
            settlement.setSettlementLevel(2);
            return settlement;
        }

        return null;
    }

    // This helper method finds a settlement belonging to a given player at a given node.
    private Settlement findPlayerSettlement(int nodeIndex, int playerNo) {
        for (Settlement settlement : settlements) {
            if (settlement.getNodeIndex() == nodeIndex && settlement.getPlayerNo() == playerNo) {
                return settlement;
            }
        }
        return null;
    }

    /**
     * This method finds the longest road length of a player.
     *
     * @param playerNo The number of the player (1-4).
     * @return The length of the longest road of the player.
     */
    public int findLongestRoadLengthOfPlayer(int playerNo) {
        int maxLength = 0;
        List<Road> playerRoads = getPlayerRoads(playerNo);

        for (Road road : playerRoads) {
            EdgeDictionaryObject edge = gameBoardDictionary.getEdge(road.getEdgeIndex());
            int length = Math.max(
                traverseRoad(playerNo, edge.getNode1_index(), new HashSet<>()),
                traverseRoad(playerNo, edge.getNode2_index(), new HashSet<>())
            );
            maxLength = Math.max(length, maxLength);
        }

        return maxLength;
    }

    /**
     * This helper method recursively traverses edges and finds maximum length without passing through the same road again.
     *
     * @param playerNo The number of the player (1-4).
     * @param currentNodeIndex The index of the current node.
     * @param traversedEdges The set of already traversed edge indices.
     * @return The maximum length of the road.
     */
    private int traverseRoad(int playerNo, int currentNodeIndex, Set<Integer> traversedEdges) {
        int maxLength = 0;
        List<Integer> adjacentEdges = gameBoardDictionary.getNode(currentNodeIndex).getAdjacentEdges();

        for (int edgeIndex : adjacentEdges) {
            if (traversedEdges.contains(edgeIndex)) {
                continue;
            }

            Road road = new Road();
            road.setEdgeIndex(edgeIndex);
            road.setPlayerNo(playerNo);

            if (roads.contains(road)) {
                Set<Integer> nextTraversedEdges = new HashSet<>(traversedEdges);
                nextTraversedEdges.add(edgeIndex);

                EdgeDictionaryObject edge = gameBoardDictionary.getEdge(edgeIndex);
                int nextNodeIndex = edge.getNode1_index() != currentNodeIndex ? edge.getNode1_index() : edge.getNode2_index();

                int length = 1 + traverseRoad(playerNo, nextNodeIndex, nextTraversedEdges);
                maxLength = Math.max(length, maxLength);
            }
        }

        return maxLength;
    }

    /**
     * This helper method gets the roads of a player.
     *
     * @param playerNo The number of the player (1-4).
     * @return The list of roads of the player.
     */
    private List<Road> getPlayerRoads(int playerNo) {
        switch (playerNo) {
            case 1:
                return player1Roads;
            case 2:
                return player2Roads;
            case 3:
                return player3Roads;
            case 4:
                return player4Roads;
            default:
                // If the player number is not valid, return an empty list
                return new ArrayList<>();
        }
    }

    // Converts the GameBoard object to a string
    @Override
    public String toString() {
        return String.format("Settlements: %s\nRoads: %s\nTiles: %s\n",
            settlementsToString(),
            roadsToString(),
            tilesToString()
        );
    }

    // Converts the settlements list to a string
    private String settlementsToString() {
        return settlements.stream()
            .map(settlement -> String.format("(nodeIndex: %d, playerNo: %d, settlementLevel: %d)",
                settlement.getNodeIndex(),
                settlement.getPlayerNo(),
                settlement.getSettlementLevel()))
            .collect(Collectors.joining(",\n", "[", "]"));
    }

    // Converts the roads list to a string
    private String roadsToString() {
        return roads.stream()
            .map(road -> String.format("(edgeIndex: %d, playerNo: %d)",
                road.getEdgeIndex(),
                road.getPlayerNo()))
            .collect(Collectors.joining(",\n", "[", "]"));
    }

    // Converts the tiles list to a string
    private String tilesToString() {
        return tiles.stream()
            .map(tile -> String.format("(tileIndex: %d, tileType: %s, tileNumber: %d)",
                tile.getTileIndex(),
                tile.getTileType(),
                tile.getTileNumber()))
            .collect(Collectors.joining(",\n", "[", "]"));
    }
}
