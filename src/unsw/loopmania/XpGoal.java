package unsw.loopmania;

public class XpGoal extends Goal {
    
    private int xpAmount;

    /**
     * Constructor for Experience goal
     * @param xpAmount
     */
    public XpGoal(int xpAmount) {

        this.xpAmount = xpAmount;
    }

    public Boolean evaluate(LoopManiaWorld world) {

        Character character = world.getCharacter();

        if (character.getXp() >= xpAmount) {

            return true;
        }

        return false;
    }
}
