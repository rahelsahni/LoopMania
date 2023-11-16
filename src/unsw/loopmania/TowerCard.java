package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class TowerCard extends Card {

    public TowerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new NonPathAdjacentPlacement());
    }

    /**
     * Spawns a tower building at the given coordinates
     * @param buildingNodeX x-coordinate of building
     * @param buildingNodeY y-coordinate of building
     * @return Building object of type Tower
     */
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new TowerBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
    
}
