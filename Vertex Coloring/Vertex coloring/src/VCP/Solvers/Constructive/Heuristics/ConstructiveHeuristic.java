package VCP.Solvers.Constructive.Heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides the methods to create and use constructive heuristics to solve vertex coloring problems.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class ConstructiveHeuristic {

    /**
     * Defines the different ways a vertex can be selected to be assigned a color.
     */
    public enum Heuristic {

        DEFAULT,
        DEGREE,
        SATURATION_DEGREE

    }

    private final Heuristic heuristic;

    /**
     * Creates a new instance of <code>ConstructiveHeuristic</code>.
     * <p>
     * @param heuristic The selection heuristic to be used by this constructive heuristic.
     */
    public ConstructiveHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public int nextVertex(int[][] adjacencyMatrix, int[] solution) {
        List<WeightedElement> wVertices;
        wVertices = new ArrayList(solution.length);
        switch (heuristic) {
            case DEFAULT:
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    if (solution[i] == -1) {
                        return i;
                    }
                }                
            case DEGREE:
                int degree;
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    if (solution[i] == -1) {
                        degree = 0;
                        for (int id : adjacencyMatrix[i]) {
                            if (solution[id] == -1) {
                                degree++;
                            }
                        }
                        wVertices.add(new WeightedElement(i, degree));
                    }
                }
                Collections.sort(wVertices, Collections.reverseOrder());
                return wVertices.get(0).getId();
            case SATURATION_DEGREE:
                int saturationDegree;
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    if (solution[i] == -1) {
                        saturationDegree = 0;
                        for (int id : adjacencyMatrix[i]) {
                            if (solution[id] >= 0) {
                                saturationDegree++;
                            }
                        }
                        wVertices.add(new WeightedElement(i, saturationDegree));
                    }
                }
                Collections.sort(wVertices, Collections.reverseOrder());
                return wVertices.get(0).getId();
        }
        return -1;
    }

    /**
     * Returns the next available id for a given vertex according to the current solution.
     * <p>
     * @param vertexId The identifier of the vertex.
     * @param adjacencyMatrix The adjacency matrix that defines the coloring problem to analyze.
     * @param solution The current solution to the VCP instance.
     * @return The next available id for a given vertex according to the current solution.
     */
    public int nextColor(int vertexId, int[][] adjacencyMatrix, int[] solution) {
        int i;
        List<Integer> colors;
        colors = new ArrayList(solution.length);
        for (int id : adjacencyMatrix[vertexId]) {
            if (solution[id] != -1 && !colors.contains(solution[id])) {
                colors.add(solution[id]);
            }
        }
        Collections.sort(colors);
        i = 0;
        for (int color : colors) {
            if (i != color) {
                return i;
            } else {
                i++;
            }
        }
        return i;
    }

    /**
     * Returns the string representation of this constructive heuristic.
     * <p>
     * @return The string representation of this constructive heuristic.
     */
    public String toString() {
        return heuristic.toString();
    }

}
