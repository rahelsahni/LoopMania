package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Helmet in the backend world
 */
public class Helmet extends BasicArmour {
    private AttackModificationStrategy attackModification;

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setDamageReductionStrategy(new DamageReductionHelmet());
        setAttackModificationStrategy(new AttackModificationHelmet());
        setPurchasePrice(300);
        setSellPrice(150);
    }

    public AttackModificationStrategy getAttackModificationStrategy() {
        return attackModification;
    }

    public void setAttackModificationStrategy(AttackModificationStrategy attackModification) {
        this.attackModification = attackModification;
    }
    
    public int modifyAttack(int attackDamage) {
        return attackModification.modifyAttack(attackDamage);
    }
}
