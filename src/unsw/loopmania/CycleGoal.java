package unsw.loopmania;

public class CycleGoal extends Goal {
    
    private int cycleAmount;

    /**
     * Constructor for CycleGoal
     * @param cycleAmount
     */
    public CycleGoal(int cycleAmount) {

        this.cycleAmount = cycleAmount;
    }

    public Boolean evaluate(LoopManiaWorld world) {

        if (world.getCycles() >= cycleAmount) {

            return true;
        }

        return false;
    }
}
