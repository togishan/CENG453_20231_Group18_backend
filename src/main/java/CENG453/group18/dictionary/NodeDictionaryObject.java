package CENG453.group18.dictionary;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class NodeDictionaryObject {
    List<Integer> adjacentNodes = new ArrayList<>();
    List<Integer> adjacentEdges = new ArrayList<>();
    List<Integer> adjacentTiles = new ArrayList<>();
}
