package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

public interface PlacementStrategy {
    public boolean isValidPlacement(Pair<Integer, Integer> buildingCoords, 
                                    List<Pair<Integer, Integer>> orderedPath, 
                                    List<Pair<Integer, Integer>> adjacent,
                                    List<Building> buildingsList);
}
