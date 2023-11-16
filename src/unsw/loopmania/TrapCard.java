package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class TrapCard extends Card {

    public TrapCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, new PathPlacement());
    }

    @Override
    public Building spawnBuilding(int buildingNodeX, int buildingNodeY) {
        return new TrapBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
    }
    
}
