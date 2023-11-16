package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped stake in the backend world
 */
public class Stake extends BasicWeapon {
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setAttackModificationStrategy(new AttackModificationStake());
        setPurchasePrice(300);
        setSellPrice(150);
    }
}
