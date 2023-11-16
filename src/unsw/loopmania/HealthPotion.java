package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a health potion in the backend world
 */
public class HealthPotion extends Item {
    private int healing;

    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setPurchasePrice(100);
        setSellPrice(50);
    }
    
    public int getHealing() {
        return healing;
    }

}
