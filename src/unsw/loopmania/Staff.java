package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped staff in the backend world
 */
public class Staff extends BasicWeapon {
    private double tranceChance;

    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setAttackModificationStrategy(new AttackModificationStaff());
        setPurchasePrice(300);
        setSellPrice(150);
        tranceChance = 0.1;
    }

    public void setTranceChance(float tranceChance) {
        this.tranceChance = tranceChance;
    }

    public double getTranceChance() {
        return tranceChance;
    }

    public boolean attemptTrance(BasicEnemy enemy) {
        double randNum = Math.random();
        if (randNum >= tranceChance) {
            return false;
        } else {
            return true;
        }
        
    }
}
