package VCP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides the methods to represent solutions for the vertex coloring problem.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class VertexColoringSolution {
    
    protected final int[] coloring;
    
    public VertexColoringSolution(int nbVertices) {
        coloring = new int[nbVertices];
        for (int i = 0; i < coloring.length; i++) {
            coloring[i] = -1;
        }
    }
    
    /**
     * Checks if the solution is complete. 
     * @return <code>true</code> if the 
     */
    public boolean isComplete() {
        for (int color : coloring) {
            if (color == -1) {
                return false;
            }
        }
        return true;
    }
    
    public int get(int id) {
        return coloring[id];
    }
    
    public void set(int id, int color) {
        coloring[id] = color;
    }
    
    /**
     * Returns the number of colors used by the current solution.
     * <p>
     * @return The number of colors used by the current solution.
     */
    public int getNbColors() {
        int nbColors;
        List<Integer> tmp;
        nbColors = 0;
        tmp = new ArrayList(coloring.length);
        for (int color : coloring) {
            if (!tmp.contains(color)) {
                tmp.add(color);
                nbColors++;
            }
        }
        return nbColors;
    }
    
    /**
     * Returns the string representation of this solution.
     * <p>
     * @return The string representation of this solution.
     */
    public String toString() {
        return Arrays.toString(coloring);
    }
    
}
