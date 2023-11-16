package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class HeroCastle extends Building {

    public HeroCastle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * Opens the shop menu when the character has completed
     * the necessary number of cycles
     * @param world
     * @param character
     */
    public void characterOnBuilding(LoopManiaWorld world, Character character) {
        // TODO Open shop menu
    }
    
}
