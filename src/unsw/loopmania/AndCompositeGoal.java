package unsw.loopmania;

import org.json.JSONArray;

public class AndCompositeGoal extends CompositeGoal {
    
    /**
     * Constructor for a goal with an AND expression
     * @param goals
     */
    public AndCompositeGoal(JSONArray goals) {

        super(goals);
    }

    public Boolean evaluate(LoopManiaWorld world) {

        for (LogicalExpression current : subGoals) {

            if (current.evaluate(world) == false) {

                return false;
            }
        }

        return true;
    }
}
