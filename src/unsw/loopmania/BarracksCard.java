package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class BarracksCard extends Card {

    public BarracksCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new PathPlacement());
    }

    /**
     * Spawns a barracks building at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type Barracks
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new BarracksBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
    
}
