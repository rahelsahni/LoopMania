package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

import org.javatuples.Pair;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {

    private PlacementStrategy placementStrategy;

    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y, PlacementStrategy placementStrategy) {
        super(x, y);
        this.placementStrategy = placementStrategy;
    }

    /**
     * Determines if the placement of the building type corresponding
     * to the card is valid. Utilises the card's placementStrategy
     * @param buildingCoords
     * @param orderedPath
     * @param adjacent
     * @param heroCastleCoords
     * @return true if building placement valid, false otherwise
     */
    public boolean isValidPlacement(Pair<Integer, Integer> buildingCoords, 
                                    List<Pair<Integer, Integer>> orderedPath, 
                                    List<Pair<Integer, Integer>> adjacent,
                                    List<Building> buildingsList) {
        return placementStrategy.isValidPlacement(buildingCoords, orderedPath, adjacent, buildingsList);
    }

    public abstract Building spawnBuilding(int buildingNodeX, int buildingNodeY);

}
