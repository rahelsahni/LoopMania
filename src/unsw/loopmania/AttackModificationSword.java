package unsw.loopmania;

public class AttackModificationSword implements AttackModificationStrategy {
    public int modifyAttack(int attackDamage) {
        return attackDamage + 20;
    }
    
}
