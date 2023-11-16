package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends Card {

    public VampireCastleCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new NonPathAdjacentPlacement());
    }    

    /**
     * Spawns a vampire castle building at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type VampireCastle
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
}
