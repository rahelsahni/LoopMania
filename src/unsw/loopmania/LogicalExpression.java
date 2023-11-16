package unsw.loopmania;

public interface LogicalExpression {
    
    /**
     * Function to add another logical expression to a child list
     * @param expression
     */
    public void add(LogicalExpression expression);

    /**
     * Function to evaluate a certain goal
     * @param world
     * @return true if goal achieved; false otherwise
     */
    public Boolean evaluate(LoopManiaWorld world);
}
