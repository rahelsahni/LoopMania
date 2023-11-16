package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Armour in the backend world
 */
public class Armour extends BasicArmour {
    // TODO = add more weapon/item types
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamageReductionStrategy(new DamageReductionArmour());
        setPurchasePrice(400);
        setSellPrice(200);
    }    
}
