package VCP;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Defines the methods to create and use vertex coloring problems.
 * <p>
 * @author José Carlos Ortiz-Bayliss (jcobayliss@gmail.com)
 * @version 2.0
 */
public class VertexColoringProblem {

    private final int nbVertices;
    private final String fileName;
    private final int[][] neighbors;

    /**
     * Creates a new instance of <code>VertexColoringProblem</code>.
     * <p>
     * @param fileName The name to load the instance from.
     */
    public VertexColoringProblem(String fileName) {
        boolean problemDefined;
        int n, j, nbEdges, u, v;
        List<Integer>[] tmp;
        String string, currentLine;
        StringTokenizer lines, words;
        string = Utils.Files.load(fileName).toLowerCase();
        lines = new StringTokenizer(string, "\n");
        problemDefined = false;
        n = 0;
        nbEdges = 0;
        tmp = new LinkedList[0];
        while (lines.hasMoreTokens()) {
            currentLine = lines.nextToken();
            switch (currentLine.substring(0, 1)) {
                case "c":
                    /*
                     * The line is a comment. It is ignored by the system.
                     */
                    break;
                case "p":
                    if (problemDefined) {
                        System.out.println("Bad input format: Only one line per file is allowed for the problem definition.");
                        System.out.println("The system will halt.");
                        System.exit(1);
                    } else {
                        problemDefined = true;
                        words = new StringTokenizer(currentLine, " \n");
                        if (words.countTokens() != 4) {
                            System.out.println("Bad input format: four elements are required in the line for the problem definition.");
                            System.out.println("The system will halt.");
                            System.exit(1);
                        } else {
                            /*
                             * The first word is discarded.
                             */
                            words.nextToken();
                            /*
                             * The problem must be 'edge'.
                             */
                            if (!words.nextToken().equalsIgnoreCase("edge")) {
                                System.out.println("Bad input format: The problem definition is not supported by the system.");
                                System.out.println("The system will halt.");
                                System.exit(1);
                            }
                            try {
                                n = Integer.parseInt(words.nextToken().trim());
                                nbEdges = Integer.parseInt(words.nextToken().trim());
                            } catch (Exception e) {
                                System.out.println("Bad input format: The number of nodes / edges is not in the proper format.");
                                System.out.println("The system will halt.");
                                System.exit(1);
                            }
                            tmp = new LinkedList[n];
                            for (int i = 0; i < n; i++) {
                                tmp[i] = new LinkedList();
                            }
                        }
                    }
                    break;
                case "e":
                    if (!problemDefined) {
                        System.out.println("Bad input format: The line for problem definition must appear before the edges definition.");
                        System.out.println("The system will halt.");
                        System.exit(1);
                    } else {
                        u = -1;
                        v = -1;
                        words = new StringTokenizer(currentLine, " \n");
                        if (words.countTokens() != 3) {
                            System.out.println("Bad input format: three elements are required per edge line.");
                            System.out.println("The system will halt.");
                            System.exit(1);
                        } else {
                            /*
                             * The first word is discarded.
                             */
                            words.nextToken();
                            try {
                                u = Integer.parseInt(words.nextToken().trim()) - 1;
                                v = Integer.parseInt(words.nextToken().trim()) - 1;
                            } catch (Exception e) {
                                System.out.println("Bad imput format: The number of edges is not in the proper format.");
                                System.out.println("The system will halt.");
                                System.exit(1);
                            }
                        }
                        /*
                         * Adds the constraints.
                         */
                        tmp[u].add(v);
                        tmp[v].add(u);
                    }
                    break;
            }
        }
        nbVertices = n;
        neighbors = new int[n][];
        for (int i = 0; i < n; i++) {
            neighbors[i] = new int[tmp[i].size()];
            j = 0;
            for (int vertex : tmp[i]) {
                neighbors[i][j++] = vertex;
            }
        }
        this.fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
     * Returns the unique identifier of this instance.
     * <p>
     * @return The unique identifier of this instance.
     */
    public String getId() {
        return fileName;
    }

    /**
     * Returns the number of vertices in this VCP instance.
     * <p>
     * @return The number of vertices in this VCP instance.
     */
    public int getSize() {
        return nbVertices;
    }

    /**
     * Returns the adjacency matrix of the vertices in this vertex coloring
     * problem.
     * <p>
     * @return The adjacency matrix of the vertices in this vertex coloring
     * problem.
     */
    public int[][] getAdjacencyMatrix() {
        int[][] tmp;
        tmp = new int[neighbors.length][neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            tmp[i] = new int[neighbors[i].length];
            System.arraycopy(neighbors[i], 0, tmp[i], 0, tmp[i].length);
        }
        return tmp;
    }

    /**
     * Returns the string representation of this vertex coloring problem.
     * <p>
     * @return The string representation of this vertex coloring problem.
     */
    public String toString() {
        StringBuilder string;
        string = new StringBuilder();
        for (int i = 0; i < neighbors.length; i++) {
            string.append("n").append(i).append(" => ").append(Arrays.toString(neighbors[i])).append("\n");
        }
        return string.toString().trim();
    }

}