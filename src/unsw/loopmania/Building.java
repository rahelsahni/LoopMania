package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Building extends StaticEntity {
    
    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public abstract void characterOnBuilding(LoopManiaWorld world, Character character);

}
