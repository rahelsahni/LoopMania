package unsw.loopmania;

public class AttackModificationAnduril implements AttackModificationStrategy {
    public int modifyAttack(int attackDamage) {
        return attackDamage + 40;
    }
}