
import GA.EvaluationFunction;
import GA.GeneticAlgorithm;
import GA.SelectionOperator;
import GA.TournamentSelectionOperator;
import HERMES.Selector.GA.DistanceBasedHeuristicSelector;
import HERMES.Selector.GA.DistanceBasedHeuristicSelectorFramework;
import HERMES.Selector.GA.DistanceBasedHeuristicSelectorIndividual;
import HERMES.Selector.HeuristicSelector;
import VCP.Solvers.Constructive.ConstructiveSolver;
import VCP.Solvers.Constructive.Heuristics.ConstructiveHeuristic;
import VCP.Solvers.Constructive.Heuristics.ConstructiveHeuristic.Heuristic;
import VCP.Solvers.Constructive.HyperHeuristic.VertexColoringEvaluationFunction;
import VCP.Solvers.Constructive.HyperHeuristic.VertexColoringEvaluationFunction.Type;
import VCP.VertexColoringProblem;
import VCP.VertexColoringProblemSet;
import VCP.VertexColoringProblemSet.Subset;
import java.util.Random;

public class Run {

    public static void main(String[] args) {
        long seed;                
        DistanceBasedHeuristicSelector hyperHeuristic;        
        VertexColoringProblemSet set;
        String[] features, heuristics;

        /*
         * Defines a seed to be used in all the random operations of the system.
         */
        seed = 12345;

        /*
         * Hyper-heuristic generation
         * =========================================================================================
         * In this example, a genetic algorithm is used to produce a
         * hyper-heuristic. You are requested to propose a new generation
         * method. Be patient, training by using a genetic algorithm takes time...
         */
        /*
         * Defines the set of instances to be used for training the
         * hyper-heuristic.
         */
        set = new VertexColoringProblemSet("../Instances/queen", Subset.TRAIN, 0.60, seed);

        /*
         * Defines thge features to be considered by the hyper-heuristic.
         */
        features = new String[]{"RND"};

        /*
         * Defines thge heuristics to be considered by the hyper-heuristic.
         */
        heuristics = new String[]{"DEG", "SDEG"};

        /*
         * Generates a hyper-heuristic by using a genetic algorithm.
         */
        hyperHeuristic = (DistanceBasedHeuristicSelector) generateOfflineHeuristicSelector(set, features, heuristics, 12345);
        /*
         * Saves the hyper-heuristic to an XML file
         */
         hyperHeuristic.save("Hyper-heuristic.xml");
        /*
         * Heuristic performance
         * =========================================================================================
         * You can test the performance of a particular heuristic on a given set
         * by using the following code.
         */
        /*
         * Defines the set of instances to be used for testing the heuristics.
         */
        set = new VertexColoringProblemSet("../Instances/queen", Subset.TEST, 0.60, seed);

        /*
         * Solves the test set by using a specific heuristic.
         */
        solve(new ConstructiveHeuristic(Heuristic.DEGREE), set);

        /*
         * Hyper-heuristic performance
         * =========================================================================================
         * You can test the performance of a hyper-heuristic on a given set by
         * using the following code.
         */
        /*
         * Solves the test set by using a hyper-heuristic.
         */
        solve(new DistanceBasedHeuristicSelector("Hyper-heuristic.xml"), set);
    }

    /**
     * Generates a hyper-heuristic by using a training set.
     * <p>
     * @param set The set to be used for generating the hyper-heuristic.
     * @param features The set of features to be considered by the
     * hyper-heuristic.
     * @param heuristics The set of heuristics to be considered by the
     * hyper-heuristic.
     * @param seed The seed to initialize the random number generator.
     * @return
     */
    public static HeuristicSelector generateOfflineHeuristicSelector(VertexColoringProblemSet set, String[] features, String[] heuristics, long seed) {
        Random random;
        SelectionOperator selectionOperator;
        EvaluationFunction evaluationFunction;
        random = new Random(seed);
        evaluationFunction = new VertexColoringEvaluationFunction(Type.RuleBasedHeuristicSelector, set);
        selectionOperator = new TournamentSelectionOperator(5, random.nextLong());
        return ((DistanceBasedHeuristicSelectorIndividual) DistanceBasedHeuristicSelectorFramework.run(features, heuristics, 50, 100, 1.0, 0.1, selectionOperator, evaluationFunction, GeneticAlgorithm.Type.GENERATIONAL, true, random.nextLong())).getHeuristicSelector();
    }

    public static void solve(ConstructiveHeuristic heuristic, VertexColoringProblemSet set) {
        StringBuilder string;
        ConstructiveSolver solver;        
        for (VertexColoringProblem problem : set.getInstances()) {
            string = new StringBuilder();
            string.append(problem.getId()).append(", ");
            solver = new ConstructiveSolver(problem);
            solver.solve(heuristic);
            string.append(solver.getNbColors());
            System.out.println(string.toString());
        }
    }

    public static void solve(DistanceBasedHeuristicSelector hyperHeuristic, VertexColoringProblemSet set) {
        StringBuilder string;
        ConstructiveSolver solver;        
        for (VertexColoringProblem problem : set.getInstances()) {
            string = new StringBuilder();            
            string.append(problem.getId()).append(", ");
            solver = new ConstructiveSolver(problem);
            solver.solve(hyperHeuristic);
            string.append(solver.getNbColors());
            System.out.println(string.toString());            
        }
    }

}
