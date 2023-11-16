package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Shield in the backend world
 */
public class Shield extends BasicArmour {
    // TODO = add more weapon/item types
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamageReductionStrategy(new DamageReductionShield());
        setPurchasePrice(400);
        setSellPrice(200);
    }

    public double criticalBiteReduction(double biteChance) {
        return biteChance * 0.4;
    }
}
