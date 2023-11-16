package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class CompositeGoal implements LogicalExpression {
    
    protected List<LogicalExpression> subGoals;

    /**
     * Constructor for a composite goal, such as AND or OR.
     * Uses recursion to create the subgoals of goals.
     * @param goals
     */
    public CompositeGoal(JSONArray goals) {

        subGoals = new ArrayList<LogicalExpression>();

        for (int i = 0; i < goals.length(); i++) {

            JSONObject current = goals.getJSONObject(i);

            LogicalExpression goal = null;

            if (current.getString("goal").equals("AND")) {

                goal = new AndCompositeGoal(current.getJSONArray("subgoals"));
            }

            else if (current.getString("goal").equals("OR")) {

                goal = new OrCompositeGoal(current.getJSONArray("subgoals"));
            }

            else {
            
                if (current.getString("goal").equals("gold")) {
    
                    goal = new GoldGoal(current.getInt("quantity"));
                }
                else if (current.getString("goal").equals("experience")) {
    
                    goal = new XpGoal(current.getInt("quantity"));
                }
                else if (current.getString("goal").equals("cycles")) {
    
                    goal = new CycleGoal(current.getInt("quantity"));
                }
                else if (current.getString("goal").equals("bosses")) {
    
                    goal = new BossGoal();
                }
            }

            add(goal);
        }
    }

    public void add(LogicalExpression expression) {

        subGoals.add(expression);
    }

    public abstract Boolean evaluate(LoopManiaWorld world);
}
