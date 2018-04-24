package HERMES.Selector;

import HERMES.FeatureManager;

/**
 * Provides a generic definition of the methods to create and use rule-based heuristic selectors
 * that map features to heuristics.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public abstract class RuleBasedHeuristicSelector extends HeuristicSelector {

    /**
     * The names of the features used by this heuristic selector.
     */
    protected String[] features;

    /**
     * Creates a new instance of <code>RuleBasedHeuristicSelector</code>.     
     */
    protected RuleBasedHeuristicSelector() {
        super();
        features =  new String[0];
    }
    
    /**
     * Creates a new instance of <code>RuleBasedHeuristicSelector</code>.
     * <p>
     * @param features The names of the features used by this heuristic selector.
     * @param heuristics The names of the heuristics used by this heuristic selector.
     */
    protected RuleBasedHeuristicSelector(String[] features, String[] heuristics) {
        super(heuristics);
        this.features = new String[features.length];
        System.arraycopy(features, 0, this.features, 0, features.length);        
    }
    
    /**
     * Returns the names of the features used by this rule-based heuristic selector.
     * <p>
     * @return The names of the features used by this rule-based heuristic selector.
     */
    public final String[] getFeatures() {
        String[] tmp;
        tmp = new String[features.length];
        System.arraycopy(features, 0, tmp, 0, features.length);
        return tmp;
    }
    
    @Override
    public abstract String getHeuristic(FeatureManager problem);
    
    /**
     * Returns the string representation of this heuristic selector.
     * <p>
     * @return The string representation of this heuristic selector.
     */
    public String toString() {
        StringBuilder string;
        string = new StringBuilder();
        string.append("Features: ");
        for (String feature : features) {
            string.append(feature).append(", ");
        }
        string.delete(string.length() - 2, string.length());
        string.append("\nHeuristics: ");
        for (String heuristic : heuristics) {
            string.append(heuristic).append(", ");
        }
        string.delete(string.length() - 2, string.length());
        return string.toString().trim();
    }    
    
}
