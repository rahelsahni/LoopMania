package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;


public class ZombiePitBuilding extends Building {

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void characterOnBuilding(LoopManiaWorld world, Character character) {
        // do nothing since the character will never be in this building
    }

}
