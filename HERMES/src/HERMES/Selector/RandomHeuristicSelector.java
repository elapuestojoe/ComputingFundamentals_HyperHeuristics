package HERMES.Selector;

import HERMES.FeatureManager;
import java.util.Random;

/**
 * Provide the methods to create and use random heuristic selectors.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class RandomHeuristicSelector extends HeuristicSelector {
    
    private final Random random;
    
    /**
     * Creates a new instance of <code>RandomHeuristicSelector</code>.
     * <p>     
     * @param heuristics The names of the heuristics used by this heuristic selector.
     * @param seed The seed to initialize the random number generator in this heuristic selector.
     */
    public RandomHeuristicSelector(String[] heuristics, long seed) {        
        super(heuristics);
        random = new Random(seed);
    }

    @Override
    public String getHeuristic(FeatureManager problem) {        
        return heuristics[random.nextInt(heuristics.length)];
    }

    @Override
    public void restart() {
        // this heuristic selector does not require to be restarted.
    }
    
}
