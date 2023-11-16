package unsw.loopmania;

public class AttackModificationHelmet implements AttackModificationStrategy {
    public int modifyAttack(int attackDamage) {
        if (attackDamage - 5 < 0) {
            return 0;
        } else {
            return attackDamage - 5;
        }
    }
    
}
