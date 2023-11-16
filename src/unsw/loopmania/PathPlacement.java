package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

public class PathPlacement implements PlacementStrategy {

    /**
     * Placement strategy for buildings that can go on path tiles
     * @param buildingCoords
     * @param orderedPath
     * @param adjacent - list of tiles adjacent to the path
     * @param heroCastleCoords
     */
    public boolean isValidPlacement(Pair<Integer, Integer> buildingCoords, 
                                    List<Pair<Integer, Integer>> orderedPath, 
                                    List<Pair<Integer, Integer>> adjacent,
                                    List<Building> buildingsList) {
        boolean onPath = false;
        for (int i = 0; i < orderedPath.size(); i++) {
            if (buildingCoords.equals(orderedPath.get(i))) {
                for (Building building: buildingsList) {
                    if (buildingCoords.equals(new Pair<Integer, Integer>(building.getX(), building.getY()))) { return false; }
                }
                onPath = true;
                break;
            }
        }
        return onPath;
    }
    
}
