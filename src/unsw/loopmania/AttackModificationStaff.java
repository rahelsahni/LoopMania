package unsw.loopmania;

public class AttackModificationStaff implements AttackModificationStrategy {
    public int modifyAttack(int attackDamage) {
        return attackDamage + 3;
    }
    
}
