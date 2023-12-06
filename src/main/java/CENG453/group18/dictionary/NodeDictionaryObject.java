package CENG453.group18.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class NodeDictionaryObject {
    List<Integer> adjacentNodes = new ArrayList<>();
    List<Integer> adjacentEdges = new ArrayList<>();
    List<Integer> adjacentTiles = new ArrayList<>();


}
