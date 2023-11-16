package unsw.loopmania;

public class BossGoal extends Goal {

    /**
     * Constructor for killing all bosses goal
     */
    public BossGoal() {}

    public Boolean evaluate(LoopManiaWorld world) {
        
        return world.getBossesKilled();
    }
}
