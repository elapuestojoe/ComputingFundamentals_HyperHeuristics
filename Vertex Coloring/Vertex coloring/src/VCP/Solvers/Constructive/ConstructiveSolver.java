package VCP.Solvers.Constructive;

import HERMES.Exceptions.NoSuchHeuristicException;
import HERMES.FeatureManager;
import HERMES.Selector.HeuristicSelector;
import VCP.Solvers.Constructive.Heuristics.ConstructiveHeuristic;
import VCP.Solvers.Constructive.Heuristics.ConstructiveHeuristic.Heuristic;
import VCP.VertexColoringProblem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Provides the methods to solve vertex solution problems.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class ConstructiveSolver implements FeatureManager {
    
    private final int[] solution;
    private final int[][] neighbors;
    
    /**
     * Creates a new instance of <code>ConstructiveSolver</code>.
     * <p>
     * @param problem The instance of the vertex solution problem to solve.
     */
    public ConstructiveSolver(VertexColoringProblem problem) {
        solution = new int[problem.getSize()];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = -1;
        }
        neighbors = problem.getAdjacencyMatrix();
    }
    
    /**
     * Returns the number of colors used by this solver.
     * <p>
     * @return The number of colors used by this solver.
     */
    public int getNbColors() {
        int nbColors;
        List<Integer> tmp;
        nbColors = 0;
        tmp = new ArrayList(solution.length);
        for (int color : solution) {
            if (!tmp.contains(color)) {
                tmp.add(color);
                nbColors++;
            }
        }
        return nbColors;
    }
    
    /**
     * Solves a vertex coloring problem by using one specific heuristic.
     * <p>
     * @param heuristic The heuristic to use.     
     */
    public void solve(ConstructiveHeuristic heuristic) {        
        int vertexId;
        while (!isSolved()) {
            vertexId = heuristic.nextVertex(neighbors, solution);
            solution[vertexId] = heuristic.nextColor(vertexId, neighbors, solution);
        }
    }
    
    /**
     * Solves a vertex coloring problem by using one specific heuristic.
     * <p>
     * @param selector The heuristic selector to use.     
     */
    public void solve(HeuristicSelector selector) {        
        int vertexId;
        ConstructiveHeuristic heuristic;
        while (!isSolved()) {
            try {
                heuristic = getHeuristic(selector.getHeuristic(this));
                vertexId = heuristic.nextVertex(neighbors, solution);
                solution[vertexId] = heuristic.nextColor(vertexId, neighbors, solution);
            } catch(NoSuchHeuristicException exception) {
                System.out.println(exception);
                System.out.println("The system will halt.");
                System.exit(1);
            }
            
        }
    }
        
    /**
     * Returns the constructive heuristic that corresponds to the string identifier provided.
     * <p>
     * @param heuristic The string identifier of the constructive heuristic to retrieve.
     * @return The constructive heuristic that corresponds to the string identifier provided.
     * @throws HERMES.Exceptions.NoSuchHeuristicException
     */
    public ConstructiveHeuristic getHeuristic(String heuristic) throws NoSuchHeuristicException {
        switch (heuristic) {
            case "DEG":
                return new ConstructiveHeuristic(Heuristic.DEGREE);
            case "SDEG":
                return new ConstructiveHeuristic(Heuristic.SATURATION_DEGREE);
            default:
                throw new NoSuchHeuristicException("Heuristic \'" + heuristic + "\' not recognized by the system.");
        }
    }   
    
    @Override
    public double getFeature(String feature) {
        Random random;
        random = new Random();
        switch (feature) {
            case "RND":
                return random.nextDouble(); 
            default:
                return Double.NaN;
        }
    }
    
    /**
     * Returns the current solution found by this solver.
     * <p>
     * @return The current solution found by this solver.
     */
    public int[] getSolution() {
        int[] tmp;
        tmp = new int[solution.length];
        System.arraycopy(solution, 0, tmp, 0, tmp.length);
        return tmp;
    }
    
    /**
     * Returns the string representation of this solution.
     * <p>
     * @return The string representation of this solution.
     */
    public String toString() {
        return Arrays.toString(solution);
    }

    /**
     * Checks if the problem is solved. 
     * @return <code>true</code> if the problem is solved. 
     */
    private boolean isSolved() {
        for (int color : solution) {
            if (color == -1) {
                return false;
            }
        }
        return true;
    }                
    
}
