package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class AlliedSoldier extends StaticEntity implements AllyAttack{

    private int health;

    public AlliedSoldier(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        health = 100;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void attackEnemy(BasicEnemy e, LoopManiaWorld world) {
        int newEnemyHealth = e.getHealthPoints() - 2;
        e.setHealthPoints(newEnemyHealth);
    }
}
