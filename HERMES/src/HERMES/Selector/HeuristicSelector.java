package HERMES.Selector;

import HERMES.FeatureManager;

/**
 * Provides a generic definition of the methods to create and use heuristic selectors.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public abstract class HeuristicSelector {
    
    /**
     * The names of the heuristics used by this heuristic selector.
     */
    protected String[] heuristics;

    /**
     * Creates a new instance of <code>HeuristicSelector</code>.     
     */
    protected HeuristicSelector() {       
       heuristics = new String[0];
    }
        
    /**
     * Creates a new instance of <code>HeuristicSelector</code>.
     * <p>     
     * @param heuristics The names of the heuristics used by this heuristic selector.
     */
    protected HeuristicSelector(String[] heuristics) {
        this.heuristics = new String[heuristics.length];
        System.arraycopy(heuristics, 0, this.heuristics, 0, heuristics.length);        
    }   

    /**
     * Returns the names of the heuristics used by this heuristic selector.
     * <p>
     * @return The names of the heuristics used by this heuristic selector.
     */
    public final String[] getHeuristics() {
        String[] tmp;
        tmp = new String[heuristics.length];
        System.arraycopy(heuristics, 0, tmp, 0, heuristics.length);
        return tmp;
    }

    /**
     * Restarts this heuristic selector.
     */
    public abstract void restart();    
    
    /**
     * Returns the heuristic to be applied given the current problem.
     * <p>
     * @param problem The problem to analyze.
     * @return The name of the heuristic to be used given the current problem.          
     */
    public abstract String getHeuristic(FeatureManager problem);

}
