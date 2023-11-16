package unsw.loopmania;

public class AttackModificationStake implements AttackModificationStrategy {
    public int modifyAttack(int attackDamage) {
        return attackDamage + 10;
    }
    
}
