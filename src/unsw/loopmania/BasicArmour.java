package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped basic armour in the backend world
 */
public class BasicArmour extends Item {
    private DamageReductionStrategy damageReduction;

    public BasicArmour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void setDamageReductionStrategy(DamageReductionStrategy damageReduction) {
        this.damageReduction = damageReduction;
    }

    public DamageReductionStrategy getDamegeReductionStrategy() {
        return damageReduction;
    }

    public int reduceDamage(int damage) {
        return damageReduction.reduceDamage(damage);
    }
}
