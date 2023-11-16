package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a gold in the backend world
 */
public class Gold extends Item {
    private int value;
    
    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
