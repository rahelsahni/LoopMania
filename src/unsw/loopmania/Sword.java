package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends BasicWeapon {
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setAttackModificationStrategy(new AttackModificationSword());
        setPurchasePrice(200);
        setSellPrice(100);
    }    
}
