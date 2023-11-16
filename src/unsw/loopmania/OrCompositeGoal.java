package unsw.loopmania;

import org.json.JSONArray;

public class OrCompositeGoal extends CompositeGoal {

    /**
     * Constructor for a goal with an OR expression
     * @param goals
     */
    public OrCompositeGoal(JSONArray goals) {

        super(goals);
    }

    public Boolean evaluate(LoopManiaWorld world) {
        
        for (LogicalExpression current : subGoals) {

            if (current.evaluate(world)) {

                return true;
            }
        }

        return false;
    }
}
