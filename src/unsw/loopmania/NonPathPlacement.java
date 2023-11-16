package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

public class NonPathPlacement implements PlacementStrategy {

    /**
     * Placement strategy for buildings that can go on any 
     * non-path tile
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
        for (Building building: buildingsList) {
            if (buildingCoords.equals(new Pair<Integer, Integer>(building.getX(), building.getY()))) { return false; }
        }
        return true;                   
    }
    
}
