package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class TheOneRing extends RareItem {

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setSellPrice(1000);
    }

    /**
     * Restores all health to character if defeated
     */
    public void revive(Character character) {
        character.setHealth(character.maxHealth);
    }
    
}
