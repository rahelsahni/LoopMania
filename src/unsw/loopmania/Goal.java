package unsw.loopmania;

public abstract class Goal implements LogicalExpression {

    public void add(LogicalExpression expression) {}

    public abstract Boolean evaluate(LoopManiaWorld world); 
}
