package unsw.loopmania;

public class GoldGoal extends Goal {
    
    private int goldAmount;

    /**
     * Constructor for Gold goal
     * @param goldAmount
     */
    public GoldGoal(int goldAmount) {

        this.goldAmount = goldAmount;
    }

    public Boolean evaluate(LoopManiaWorld world) {

        Character character = world.getCharacter();

        if (character.getGold().getValue() >= goldAmount) {

            return true;
        }

        return false;
    }
}
