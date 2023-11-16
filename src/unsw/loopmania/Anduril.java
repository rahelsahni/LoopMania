package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class Anduril extends RareItem {
    private AttackModificationStrategy attackModification;

    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setAttackModificationStrategy(new AttackModificationAnduril());
        setSellPrice(500);
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
