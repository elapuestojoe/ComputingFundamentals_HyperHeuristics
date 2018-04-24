package HERMES.Selector.GA;

import HERMES.Selector.HeuristicSelector;
import GA.Individual;
import java.text.DecimalFormat;
import java.util.BitSet;

/**
 * Provides the methods to create and use individuals that code vector heuristic
 * selectors to be evolved by a genetic algorithm.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public class DistanceBasedHeuristicSelectorIndividual extends Individual {

    private int numberOfGenes;
    private final int bitsPerGene, bitsPerFeature, bitsPerHeuristic;
    private final BitSet chromosome;
    private static final double MIN_VALUE = -0.10, MAX_VALUE = 1.10;
    private static String[] featureNames = new String[0], heuristicNames = new String[0];

    /**
     * Sets the names of the heuristics to be used by all rule-based
     * individuals.
     * <p>
     * @param featureNames The names of the features to be used by all
     * rule-based individuals.
     */
    public static void setFeatureNames(String[] featureNames) {
        DistanceBasedHeuristicSelectorIndividual.featureNames = featureNames;
    }

    /**
     * Sets the names of the heuristics to be used by all the individuals.
     * <p>
     * @param heuristicNames The names of the heuristics to be used by all
     * the individuals.
     */
    public static void setHeuristicNames(String[] heuristicNames) {
        DistanceBasedHeuristicSelectorIndividual.heuristicNames = heuristicNames;
    }

    /**
     * Creates a new instance of <code>DistanceBasedHeuristicSelectorIndividual</code>.
     * <p>
     * @param numberOfGenes The number of genes in this individual.
     * @param bitsPerFeature The number of bits used to represent each feature.
     * @param seed The seed to initialize the random number generator to be used
     * in all the random operations in this individual.
     */
    public DistanceBasedHeuristicSelectorIndividual(int numberOfGenes, int bitsPerFeature, long seed) {
        super(0, seed);
        int bitCounter;
        BitSet bits;
        this.numberOfGenes = numberOfGenes;
        this.bitsPerFeature = bitsPerFeature;
        bitsPerHeuristic = (int) Math.ceil(Math.log(heuristicNames.length) / Math.log(2));
        this.bitsPerGene = bitsPerFeature * featureNames.length + bitsPerHeuristic;
        bitCounter = 0;
        chromosome = new BitSet();
        for (int i = 0; i < numberOfGenes; i++) {
            /*
             * Initializes the values of the features.
             */
            for (String featureName : featureNames) {
                double temp = random.nextDouble();
                bits = toBitSet(temp, bitsPerFeature);
                for (int k = 0; k < bitsPerFeature; k++) {
                    chromosome.set(bitCounter++, bits.get(k));
                }
            }
            /*
             * Initializes the index of the heuristic.
             */
            for (int j = 0; j < bitsPerHeuristic; j++) {
                chromosome.set(bitCounter++, random.nextBoolean());
            }
        }
    }

    /**
     * Creates a new instance of <code>DistanceBasedHeuristicSelectorIndividual</code>.
     * <p>
     * @param individual The instance of
     * <code>DistanceBasedHeuristicSelectorIndividual</code> to copy to this individual.
     */
    protected DistanceBasedHeuristicSelectorIndividual(DistanceBasedHeuristicSelectorIndividual individual) {
        super(individual.getEvaluation(), individual.random.nextLong());
        numberOfGenes = individual.numberOfGenes;
        bitsPerFeature = individual.bitsPerFeature;
        bitsPerHeuristic = individual.bitsPerHeuristic;
        bitsPerGene = bitsPerFeature * featureNames.length + bitsPerHeuristic;
        chromosome = (BitSet) individual.chromosome.clone();
    }

    /**
     * Returns the heuristic selector coded within this individual.
     * <p>
     * @return The heuristic selector coded within this individual.
     */
    public HeuristicSelector getHeuristicSelector() {
        int[] output;
        double[][] input;
        BitSet gene, featureBitSet, heuristicBitSet;
        input = new double[numberOfGenes][featureNames.length];
        output = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++) {
            gene = chromosome.get(i * bitsPerGene, (i + 1) * bitsPerGene);
            /*
             * Converts to string the information about the features.
             */
            for (int j = 0; j < featureNames.length; j++) {
                featureBitSet = gene.get(j * bitsPerFeature, (j + 1) * bitsPerFeature);
                input[i][j] = toDouble(featureBitSet, bitsPerFeature);
            }
            heuristicBitSet = gene.get(bitsPerFeature * featureNames.length, bitsPerFeature * featureNames.length + bitsPerHeuristic);
            output[i] = toInteger(heuristicBitSet) % heuristicNames.length;
        }
        return new DistanceBasedHeuristicSelector(featureNames, heuristicNames, input, output);
    }

    @Override
    public DistanceBasedHeuristicSelectorIndividual[] combine(Individual[] individuals, double crossoverRate) {
        int crossoverPoint, crossoverPointA, crossoverPointB, crossoverGeneA, corssoverGeneB, j;
        DistanceBasedHeuristicSelectorIndividual parent;
        parent = (DistanceBasedHeuristicSelectorIndividual) individuals[1];
        /*
         * Verifies if the individuals can be combined.
         */
        if (bitsPerFeature != parent.bitsPerFeature) {
            System.out.println("The number of bits to represent features is not the same in both individuals. Crossover cannot take place.");
            System.out.println("The system will halt.");
            System.exit(1);
        }
        if (bitsPerHeuristic != parent.bitsPerHeuristic) {
            System.out.println("The number of bits to represent available heuristics is not the same in both individuals. Crossover cannot take place.");
            System.out.println("The system will halt.");
            System.exit(1);
        }
        crossoverPoint = random.nextInt(bitsPerGene);
        crossoverGeneA = random.nextInt(this.numberOfGenes);
        corssoverGeneB = random.nextInt(parent.numberOfGenes);
        crossoverPointA = crossoverPoint + crossoverGeneA * bitsPerGene;
        crossoverPointB = crossoverPoint + corssoverGeneB * bitsPerGene;
        chromosome.set(crossoverPointA, bitsPerGene * numberOfGenes, false);
        j = crossoverPointB;
        numberOfGenes = crossoverGeneA + (parent.numberOfGenes - corssoverGeneB);
        for (int i = crossoverPointA; i < bitsPerGene * numberOfGenes; i++) {
            chromosome.set(i, parent.chromosome.get(j++));
        }
        return new DistanceBasedHeuristicSelectorIndividual[]{this};
    }

    @Override
    public DistanceBasedHeuristicSelectorIndividual mutate(double mutationRate) {
        for (int i = 0; i < bitsPerGene * numberOfGenes; i++) {
            if (random.nextDouble() < mutationRate) {
                chromosome.flip(i);
            }
        }
        return this;
    }

    @Override
    public Individual copy() {
        return new DistanceBasedHeuristicSelectorIndividual(this);
    }

    @Override
    public String toString() {
        BitSet gene;
        StringBuilder string;
        string = new StringBuilder();
        for (int i = 0; i < numberOfGenes; i++) {
            string.append("[");
            gene = chromosome.get(i * bitsPerGene, (i + 1) * bitsPerGene);
            string.append(geneToString(gene));
            string.deleteCharAt(string.length() - 1);
            string.append("]\n");
        }
        return string.toString().trim();
    }

    /**
     * Returns the string representation of the gene provided.
     * <p/>
     * @param gene The gene whose string representation is required.
     * @return The string representation of the gene provided.
     */
    private String geneToString(BitSet gene) {
        BitSet featureBitSet, heuristicBitSet;
        DecimalFormat format;
        format = new DecimalFormat("0.00000");
        StringBuilder string, tempString;
        string = new StringBuilder();
        /*
         * Converts to string the information about the features.
         */
        for (int i = 0; i < featureNames.length; i++) {
            featureBitSet = gene.get(i * bitsPerFeature, (i + 1) * bitsPerFeature);
            tempString = new StringBuilder();
            for (int j = 0; j < bitsPerFeature; j++) {
                tempString.append(featureBitSet.get(j) ? "1" : "0");
            }
            string.append(tempString.reverse().toString()).append(" (").append(format.format(toDouble(featureBitSet, bitsPerFeature))).append(") ");
        }
        /*
         * Converts to string the inforamtion about the heuristic.
         */
        tempString = new StringBuilder();
        heuristicBitSet = gene.get(bitsPerFeature * featureNames.length, bitsPerFeature * featureNames.length + bitsPerHeuristic);
        for (int i = 0; i < bitsPerHeuristic; i++) {
            tempString.append(heuristicBitSet.get(i) ? "1" : "0");
        }
        string.append(tempString.reverse().toString()).append(" (").append(toInteger(heuristicBitSet)).append(") ");
        return string.toString();
    }

    /**
     * Returns the value as integer of the bit set provided as argument.
     * <p/>
     * @param bitset The bit set whose integer value is required.
     * @return The value as integer of the bit set provided as argument.
     */
    private static int toInteger(BitSet bits) {
        int i, value;
        value = 0;
        i = bits.nextSetBit(0);
        while (i >= 0) {
            value += Math.pow(2, i);
            i = bits.nextSetBit(i + 1);
        }
        return value;
    }

    /**
     * Returns the double value of the bit set provided as argument.
     * <p/>
     * @param bitset The bit set whose integer value is required.
     * @param numberOfBits The maximum number of bits that can be used to
     * represent the value.
     * @return The value as double of the bit set provided as argument.
     */
    private static double toDouble(BitSet bits, int numberOfBits) {
        int numberOfSteps;
        double stepSize;
        stepSize = (MAX_VALUE - MIN_VALUE) / Math.pow(2, numberOfBits);
        numberOfSteps = toInteger(bits);
        return MIN_VALUE + (stepSize / 2) + numberOfSteps * stepSize;
    }

    private static BitSet toBitSet(int value) {
        int i;
        BitSet bits;
        bits = new BitSet();
        i = 0;
        while (value > 0) {
            bits.set(i++, (value % 2 != 0));
            value = value / 2;
        }
        return bits;
    }

    /**
     * @param numberOfBits The maximum number of bits that can be used to
     * represent the value.
     */
    private static BitSet toBitSet(double value, int numberOfBits) {
        double stepSize;
        if (value < MIN_VALUE) {
            value = MIN_VALUE;
        }
        if (value > MAX_VALUE) {
            value = MAX_VALUE;
        }
        stepSize = (MAX_VALUE - MIN_VALUE) / Math.pow(2, numberOfBits);
        return toBitSet((int) Math.round((value + Math.abs(MIN_VALUE + stepSize / 2)) / stepSize));
    }

}
