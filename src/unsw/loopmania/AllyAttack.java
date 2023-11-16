package unsw.loopmania;

/**
 * A simple interface for allies to attack enemies
 */
public interface AllyAttack {
    public void attackEnemy(BasicEnemy e, LoopManiaWorld world);
}