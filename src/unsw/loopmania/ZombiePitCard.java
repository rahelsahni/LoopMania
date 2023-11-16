package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a vampire castle card in the backend game world
 */
public class ZombiePitCard extends Card {

    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new NonPathAdjacentPlacement());
    }    

    /**
     * Spawns a barracks building at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type ZombiePit
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
}
