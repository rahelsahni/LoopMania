package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class BarracksBuilding extends Building {

    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * When the character encounters the barracks, 
     * an allied soldier is added to the world.
     * @param world
     * @param character
     */
    public void characterOnBuilding(LoopManiaWorld world, Character character) {
        world.addAlliedSoldier();        
    }
    
}
