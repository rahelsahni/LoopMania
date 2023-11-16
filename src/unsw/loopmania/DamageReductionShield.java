package unsw.loopmania;

public class DamageReductionShield implements DamageReductionStrategy {
    public int reduceDamage(int damage) {
        return (int)Math.round(damage*2.0/3.0);
    }
}
