package unsw.loopmania;

public class DamageReductionTreeStump implements DamageReductionStrategy {
    public int reduceDamage(int damage) {
        return (int)Math.round(damage*1.0/3.0);
    }
}
