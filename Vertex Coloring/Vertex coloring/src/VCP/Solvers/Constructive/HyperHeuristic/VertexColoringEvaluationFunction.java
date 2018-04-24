package VCP.Solvers.Constructive.HyperHeuristic;

import GA.EvaluationFunction;
import GA.Individual;
import HERMES.Selector.GA.DistanceBasedHeuristicSelectorIndividual;
import HERMES.Selector.HeuristicSelector;
import VCP.Solvers.Constructive.ConstructiveSolver;
import VCP.VertexColoringProblem;
import VCP.VertexColoringProblemSet;

/**
 * Provides a way to evaluate the performance of heuristic selectors designed
 * for solving vertex coloring problems.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class VertexColoringEvaluationFunction implements EvaluationFunction {

    /**
     * Defines the available types of heuristic selectors to be evaluated.
     */
    public enum Type {

        /**
         * A rule-based heuristic selector where the condition contains the
         * description of the instance and the action represents one heuristic.
         */
        RuleBasedHeuristicSelector
    }

    private final Type type;
    private final VertexColoringProblem[] problems;

    /**
     * Creates a new instance of <code>VertexColoringEvaluationFunction</code>.
     * <p>
     * @param type The type of the heuristic selector to evaluate.
     * @param set The that contains the CSP instances to solve. each instance
     * (in milliseconds).
     */
    public VertexColoringEvaluationFunction(Type type, VertexColoringProblemSet set) {
        this.type = type;
        problems = set.getInstances();
    }

    /**
     * Returns the instances used for the generation of the heuristic selector.
     * <p/>
     * @return The instances used for the generation of the heuristic selector.
     */
    public VertexColoringProblem[] getInstances() {
        return problems;
    }

    @Override
    public double evaluate(Individual individual) {
        double nbColors;
        HeuristicSelector selector = null;
        ConstructiveSolver solver;
        switch (type) {
            case RuleBasedHeuristicSelector:
                selector = ((DistanceBasedHeuristicSelectorIndividual) individual).getHeuristicSelector();
                break;
            default:
                System.out.println("The type of evaluation is not supported by the system.");
                System.out.println("The system will halt.");
                System.exit(1);
        }
        nbColors = 0;
        for (VertexColoringProblem problem : problems) {
            solver = new ConstructiveSolver(problem);
            solver.solve(selector);
            nbColors += solver.getNbColors();
        }
        return nbColors / problems.length;
    }

}
