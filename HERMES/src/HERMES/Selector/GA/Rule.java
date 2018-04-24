package HERMES.Selector.GA;

import java.util.Arrays;

/**
 * Provides the methods to create and use rules by the rule-based hyper-heuristic framework.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 2.0
 */
public class Rule {
    
    private static double gamma = 0;
    
    private final double[] condition;
    private final int actionId;

    /**
     * Sets the value of gamma for using a radial kernel as distance measure.
     * <p>
     * @param gamma The gamma value to be used by the radial kernel.
     */
    public static void setGamma(double gamma) {
        Rule.gamma = gamma;
    }
    
    /**
     * Creates a new instance of <code>Rule</code>.
     * <p>     
     * @param condition The condition of the rule.
     * @param actionId The identifier of the action to apply when this rule is satisfied.
     */
    public Rule(double[] condition, int actionId) {        
        this.condition = condition;
        this.actionId = actionId;
    }   
    
    /**
     * Returns the condition of this rule.
     * <p>
     * @return The condition of this rule.
     */
    public double[] getCondition() {
        return condition;
    }
    
    /**
     * Returns the identifier of action to apply if the condition of this rule is satisfied.
     * <p>
     * @return The identifier of action to apply if the condition of this rule is satisfied.
     */
    public int getActionId() {
        return actionId;
    }           

    /**
     * Returns the Euclidean distance between the condition of this rule and the current problem state.
     * <p>
     * @param state The current problem state.
     * @return The Euclidean distance between the condition of this rule and the current problem state. In this implementation, <code>NaN</code>
     * do not contribute to the distance.
     */
    public double distance(double[] state) {
        double distance;
        if (condition.length != state.length) {
            System.out.println("The dimension of the condition is different to the dimension of the problem state.");
            System.out.println("The system will halt.");
            System.exit(1);
        }
        distance = 0;        
        for (int i = 0; i < condition.length; i++) {
            if (!Double.isNaN(condition[i]) && !Double.isNaN(state[i])) {
                distance += Math.pow(condition[i] - state[i], 2);
            }
        }        
        if (gamma > 0) {
            //double gamma = 1.0 / condition.length;
            return 2 - 2 * Math.exp(-gamma * distance);
        } else {        
            return Math.sqrt(distance);               
        }
    }
    
    /**
     * Returns the string representation of this rule.
     * <p>
     * @return The string representation of this rule.
     */
    public String toString() {
        StringBuilder string;
        string = new StringBuilder();
        string.append(Arrays.toString(condition)).append(" => ").append(Arrays.toString(condition));
        return string.toString();
    }
    
}
