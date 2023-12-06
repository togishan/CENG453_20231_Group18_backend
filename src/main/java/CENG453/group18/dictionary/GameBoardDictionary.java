package CENG453.group18.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class GameBoardDictionary {
    HashMap<Integer, NodeDictionaryObject> nodes = new HashMap<>();
    HashMap<Integer, EdgeDictionaryObject> edges = new HashMap<>();

    public GameBoardDictionary()
    {
        nodes.put(0, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(3, 4)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(0, 1)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0))     // adjacentTiles
        ));
        nodes.put(1, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(4, 5)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(2, 3)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(1))     // adjacentTiles
        ));
        nodes.put(2, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(5, 6)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(4, 5)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(2))     // adjacentTiles
        ));
        nodes.put(3, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(0, 7)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(0, 6)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0))     // adjacentTiles
        ));
        nodes.put(4, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(0, 1)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(1, 2, 7)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0, 1))     // adjacentTiles
        ));
        nodes.put(5, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(1, 2)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(3, 4, 8)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(1, 2))     // adjacentTiles
        ));
        nodes.put(6, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(2, 10)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(5, 9)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(2))     // adjacentTiles
        ));
        nodes.put(7, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(3, 11, 12)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(6, 10, 11)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0, 3))     // adjacentTiles
        ));
        nodes.put(8, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(4, 12, 13)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(7, 12, 13)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0, 1 ,4))     // adjacentTiles
        ));
        nodes.put(9, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(5, 13, 14)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(8, 14, 15)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(1, 2, 5))     // adjacentTiles
        ));
        nodes.put(10, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(6, 14, 15)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(9, 16, 17)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(2, 6))     // adjacentTiles
        ));
        nodes.put(11, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(7, 16)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(10, 18)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(3))     // adjacentTiles
        ));
        nodes.put(12, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(7, 8, 17)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(11, 12, 19)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(0, 3, 4))     // adjacentTiles
        ));
        nodes.put(13, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(8, 9, 18)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(13, 14, 20)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(1, 4, 5))     // adjacentTiles
        ));
        nodes.put(14, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(9, 10, 19)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(15, 16, 21)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(2, 5, 6))     // adjacentTiles
        ));
        nodes.put(15, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(10, 20)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(17, 22)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(6))     // adjacentTiles
        ));
        nodes.put(16, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(11, 21, 22)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(18, 23, 24)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(3, 7))     // adjacentTiles
        ));
        nodes.put(17, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(12, 22, 23)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(19, 25, 26)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(3, 4, 8))     // adjacentTiles
        ));
        nodes.put(18, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(13, 23, 24)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(20, 27, 28)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(4, 5, 9))     // adjacentTiles
        ));
        nodes.put(19, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(14, 24, 25)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(21, 29, 30)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(5, 6, 10))     // adjacentTiles
        ));
        nodes.put(20, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(15, 25, 26)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(22, 31, 32)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(6, 11))     // adjacentTiles
        ));
        nodes.put(21, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(16, 27)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(23, 33)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(7))     // adjacentTiles
        ));
        nodes.put(22, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(16, 17, 28)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(24, 25, 34)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(3, 7, 8))     // adjacentTiles
        ));
        nodes.put(23, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(17, 18, 29)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(26, 27, 35)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(4, 8, 9))     // adjacentTiles
        ));
        nodes.put(24, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(18, 19, 30)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(28, 29, 36)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(5, 9, 10))     // adjacentTiles
        ));
        nodes.put(25, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(19, 20, 31)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(30, 31, 37)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(6, 10, 11))     // adjacentTiles
        ));
        nodes.put(26, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(20, 32)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(32, 38)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(11))     // adjacentTiles
        ));
        nodes.put(27, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(21, 33)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(33, 39)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(7))     // adjacentTiles
        ));
        nodes.put(28, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(22, 33, 34)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(34, 40, 41)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(7, 8, 12))     // adjacentTiles
        ));
        nodes.put(29, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(23, 34, 35)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(35, 42, 43)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(8, 9, 13))     // adjacentTiles
        ));
        nodes.put(30, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(24, 35, 36)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(36, 44, 45)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(9, 10, 14))     // adjacentTiles
        ));
        nodes.put(31, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(25, 36, 37)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(37, 46, 47)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(10, 11, 15))     // adjacentTiles
        ));
        nodes.put(32, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(26, 37)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(38, 48)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(11))     // adjacentTiles
        ));
        nodes.put(33, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(27, 28, 38)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(39, 40, 49)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(7, 12))     // adjacentTiles
        ));
        nodes.put(34, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(28, 29, 39)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(41, 42, 50)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(8, 12, 13))     // adjacentTiles
        ));
        nodes.put(35, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(29, 30, 40)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(43, 44, 51)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(9, 13, 14))     // adjacentTiles
        ));
        nodes.put(36, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(30, 31, 41)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(45, 46, 52)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(10, 14, 15))     // adjacentTiles
        ));
        nodes.put(37, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(31, 32, 42)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(47, 48, 53)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(11, 15))     // adjacentTiles
        ));
        nodes.put(38, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(33, 43)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(49, 54)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(12))     // adjacentTiles
        ));
        nodes.put(39, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(34, 43, 44)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(50, 55, 56)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(12, 13, 16))     // adjacentTiles
        ));
        nodes.put(40, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(35, 44, 45)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(51, 57, 58)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(13, 14, 17))     // adjacentTiles
        ));
        nodes.put(41, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(36, 45, 46)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(52, 59, 60)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(14, 15, 18))     // adjacentTiles
        ));
        nodes.put(42, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(37, 46)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(53, 61)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(15))     // adjacentTiles
        ));
        nodes.put(43, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(38, 39, 47)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(54, 55, 62)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(12, 16))     // adjacentTiles
        ));
        nodes.put(44, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(39, 40, 48)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(56, 57, 63)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(13, 16, 17))     // adjacentTiles
        ));
        nodes.put(45, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(40, 41, 49)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(58, 59, 64)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(14, 17, 18))     // adjacentTiles
        ));
        nodes.put(46, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(41, 42, 50)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(60, 61, 65)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(15, 18))     // adjacentTiles
        ));
        nodes.put(47, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(43, 51)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(62, 66)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(16))     // adjacentTiles
        ));
        nodes.put(48, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(44, 51, 52)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(63, 67, 68)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(16, 17))     // adjacentTiles
        ));
        nodes.put(49, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(45, 52, 53)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(64, 69, 70)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(17, 18))     // adjacentTiles
        ));
        nodes.put(50, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(46, 53)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(65, 71)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(18))     // adjacentTiles
        ));
        nodes.put(51, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(47, 48)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(66, 67)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(16))     // adjacentTiles
        ));
        nodes.put(52, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(48, 49)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(68, 69)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(17))     // adjacentTiles
        ));
        nodes.put(53, new NodeDictionaryObject(
                new ArrayList<Integer>(Arrays.asList(49, 50)),    // adjacentNodes
                new ArrayList<Integer>(Arrays.asList(70, 71)),    // adjacentEdges
                new ArrayList<Integer>(Arrays.asList(18))     // adjacentTiles
        ));


        edges.put(0, new EdgeDictionaryObject(0,3));
        edges.put(1, new EdgeDictionaryObject(0,4));
        edges.put(2, new EdgeDictionaryObject(1,4));
        edges.put(3, new EdgeDictionaryObject(1,5));
        edges.put(4, new EdgeDictionaryObject(2,5));
        edges.put(5, new EdgeDictionaryObject(2,6));
        edges.put(6, new EdgeDictionaryObject(3,7));
        edges.put(7, new EdgeDictionaryObject(4,8));
        edges.put(8, new EdgeDictionaryObject(5,9));
        edges.put(9, new EdgeDictionaryObject(6,10));
        edges.put(10, new EdgeDictionaryObject(7,11));
        edges.put(11, new EdgeDictionaryObject(7,12));
        edges.put(12, new EdgeDictionaryObject(8,12));
        edges.put(13, new EdgeDictionaryObject(8,13));
        edges.put(14, new EdgeDictionaryObject(9,13));
        edges.put(15, new EdgeDictionaryObject(9,14));
        edges.put(16, new EdgeDictionaryObject(10,14));
        edges.put(17, new EdgeDictionaryObject(10,15));
        edges.put(18, new EdgeDictionaryObject(11,16));
        edges.put(19, new EdgeDictionaryObject(12,17));
        edges.put(20, new EdgeDictionaryObject(13,18));
        edges.put(21, new EdgeDictionaryObject(14,19));
        edges.put(22, new EdgeDictionaryObject(15,20));
        edges.put(23, new EdgeDictionaryObject(16,21));
        edges.put(24, new EdgeDictionaryObject(16,22));
        edges.put(25, new EdgeDictionaryObject(17,22));
        edges.put(26, new EdgeDictionaryObject(17,23));
        edges.put(27, new EdgeDictionaryObject(18,23));
        edges.put(28, new EdgeDictionaryObject(18,24));
        edges.put(29, new EdgeDictionaryObject(19,24));
        edges.put(30, new EdgeDictionaryObject(19,25));
        edges.put(31, new EdgeDictionaryObject(20,25));
        edges.put(32, new EdgeDictionaryObject(20,26));
        edges.put(33, new EdgeDictionaryObject(21,27));
        edges.put(34, new EdgeDictionaryObject(22,28));
        edges.put(35, new EdgeDictionaryObject(23,29));
        edges.put(36, new EdgeDictionaryObject(24,30));
        edges.put(37, new EdgeDictionaryObject(25,31));
        edges.put(38, new EdgeDictionaryObject(26,32));
        edges.put(39, new EdgeDictionaryObject(27,33));
        edges.put(40, new EdgeDictionaryObject(28,33));
        edges.put(41, new EdgeDictionaryObject(28,34));
        edges.put(42, new EdgeDictionaryObject(29,34));
        edges.put(43, new EdgeDictionaryObject(29,35));
        edges.put(44, new EdgeDictionaryObject(30,35));
        edges.put(45, new EdgeDictionaryObject(30,36));
        edges.put(46, new EdgeDictionaryObject(31,36));
        edges.put(47, new EdgeDictionaryObject(31,37));
        edges.put(48, new EdgeDictionaryObject(32,37));
        edges.put(49, new EdgeDictionaryObject(33,38));
        edges.put(50, new EdgeDictionaryObject(34,39));
        edges.put(51, new EdgeDictionaryObject(35,40));
        edges.put(52, new EdgeDictionaryObject(36,41));
        edges.put(53, new EdgeDictionaryObject(37,42));
        edges.put(54, new EdgeDictionaryObject(38,43));
        edges.put(55, new EdgeDictionaryObject(39,43));
        edges.put(56, new EdgeDictionaryObject(39,44));
        edges.put(57, new EdgeDictionaryObject(40,44));
        edges.put(58, new EdgeDictionaryObject(40,45));
        edges.put(59, new EdgeDictionaryObject(41,45));
        edges.put(60, new EdgeDictionaryObject(41,46));
        edges.put(61, new EdgeDictionaryObject(42,46));
        edges.put(62, new EdgeDictionaryObject(43,47));
        edges.put(63, new EdgeDictionaryObject(44,48));
        edges.put(64, new EdgeDictionaryObject(45,49));
        edges.put(65, new EdgeDictionaryObject(46,50));
        edges.put(66, new EdgeDictionaryObject(47,51));
        edges.put(67, new EdgeDictionaryObject(48,51));
        edges.put(68, new EdgeDictionaryObject(48,52));
        edges.put(69, new EdgeDictionaryObject(49,52));
        edges.put(70, new EdgeDictionaryObject(49,53));
        edges.put(71, new EdgeDictionaryObject(50,53));
    }

    public NodeDictionaryObject getNode(int index)
    {
        return nodes.get(index);
    }

    public EdgeDictionaryObject getEdge(int index)
    {
        return edges.get(index);
    }
}
