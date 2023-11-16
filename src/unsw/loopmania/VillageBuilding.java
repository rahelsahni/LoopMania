package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class VillageBuilding extends Building {

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * When the character encounters a village, 
     * 50 HP is restored to the character's health 
     * @param world
     * @param character
     */
    public void characterOnBuilding(LoopManiaWorld world, Character character) {
        character.setHealth(character.getHealth() + 50);
    }
    
}
