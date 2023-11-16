package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped basic weapon in the backend world
 */
public class BasicWeapon extends Item {
    private AttackModificationStrategy attackModification;

    public BasicWeapon(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
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