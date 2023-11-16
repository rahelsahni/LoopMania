package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class CampfireCard extends Card {

    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new NonPathPlacement());
    }

    /**
     * Spawns a campfire at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type Campfire
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new CampfireBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
    
}
