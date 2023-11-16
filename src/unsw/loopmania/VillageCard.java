package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a village card in the backend game world
 */
public class VillageCard extends Card {

    public VillageCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new PathPlacement());
    }

    /**
     * Spawns a village building at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type Village
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new VillageBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
    
}
