package unsw.loopmania;

public class DamageReductionArmour implements DamageReductionStrategy {
    public int reduceDamage(int damage) {
        return (int)Math.round(damage*0.5);
    }
}
