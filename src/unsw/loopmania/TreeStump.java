package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class TreeStump extends RareItem {
    private DamageReductionStrategy damageReduction;

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamageReductionStrategy(new DamageReductionTreeStump());
        setSellPrice(500);
    }

    public DamageReductionStrategy getDamegeReductionStrategy() {
        return damageReduction;
    }

    public void setDamageReductionStrategy(DamageReductionStrategy damageReduction) {
        this.damageReduction = damageReduction;
    }
    
    public int reduceDamage(int damage) {
        return damageReduction.reduceDamage(damage);
    }
}
