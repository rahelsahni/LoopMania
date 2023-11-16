package unsw.loopmania;

public class DamageReductionHelmet implements DamageReductionStrategy {
    public int reduceDamage(int damage) {
        return damage - 15;
    }
}
