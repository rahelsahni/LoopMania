package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

public class NonPathAdjacentPlacement implements PlacementStrategy {

    /**
     * Placement strategy for buildings that can go on non-path
     * tiles adjacent to the path
     * @param buildingCoords
     * @param orderedPath
     * @param adjacent - list of tiles adjacent to the path
     * @param heroCastleCoords
     */
    public boolean isValidPlacement(Pair<Integer, Integer> buildingCoords, 
                                    List<Pair<Integer, Integer>> orderedPath,
                                    List<Pair<Integer, Integer>> adjacent,
                                    List<Building> buildingsList) {
        for (int i = 0; i < orderedPath.size(); i++) {
            if (buildingCoords.equals(orderedPath.get(i))) {
                return false;
            }
        }
        if (adjacent.size() == 0) {
            return false;
        }
        for (Building building: buildingsList) {
            if (buildingCoords.equals(new Pair<Integer, Integer>(building.getX(), building.getY()))) { return false; }
        }
        return true;
    }
    
}
