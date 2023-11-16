package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class TowerBuilding extends Building implements AllyAttack {

    private int supportRadius;

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        supportRadius = 3;
    }

    public void characterOnBuilding(LoopManiaWorld world, Character character) {
        // do nothing since the character will never be in this building
    }
    
    public int getSupportRadius() {
        return supportRadius;
    }

    public void attackEnemy(BasicEnemy e, LoopManiaWorld world) {
        int newEnemyHealth = e.getHealthPoints() - 5;
        e.setHealthPoints(newEnemyHealth);
    }
}
