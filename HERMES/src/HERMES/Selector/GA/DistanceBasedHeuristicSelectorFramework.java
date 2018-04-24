package HERMES.Selector.GA;

import GA.EvaluationFunction;
import GA.GeneticAlgorithm;
import GA.GeneticAlgorithm.Type;
import GA.Individual;
import GA.SelectionOperator;
import java.util.Random;

/**
 * Provides the methods to run the evolutionary framework to produce distance-based heuristic
 * selectors.
 * <p>
 * @author José Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 1.0
 */
public abstract class DistanceBasedHeuristicSelectorFramework {        
    
    /**
     * Runs the genetic algorithm-based framework to produce distance-based heuristic selectors.
     * <p>
     * @param featureNames The names of the features to be used by the resulting vector heuristic selector.
     * @param heuristicNames The names of the heuristics to be used by the resulting vector heuristic selector.          
     * @param populationSize The number of individuals in the population.
     * @param nbCycles The number of cycles the genetic algorithm will run.
     * @param crossoverRate The crossover rate to be used by the genetic algorithm.
     * @param mutationRate The mutation rate to be used by the genetic algorithm.
     * @param selectionOperator The selection operator to be used by the genetic algorithm.
     * @param evaluationFunction The evaluation function to be used by the genetic algorithm.     
     * @param type The type of the genetic algorithm to use. 
     * @param printMode A flag to indicate whether or not the progress of the genetic algorithm should be printed on screen.
     * @param seed The seed to initialize the random number generator.
     * @return The heuristic selector resulting from running the framework.
     */
    public static Individual run(String[] featureNames, String[] heuristicNames, int populationSize, int nbCycles, double crossoverRate, double mutationRate, SelectionOperator selectionOperator, EvaluationFunction evaluationFunction, Type type, boolean printMode, long seed) {                        
        return run(featureNames, heuristicNames, populationSize, nbCycles, crossoverRate, mutationRate, selectionOperator, evaluationFunction, type, printMode, Double.NaN, seed);
    }
    
    /**
     * Runs the genetic algorithm-based framework to produce distance-based heuristic selectors.
     * <p>
     * @param featureNames The names of the features to be used by the resulting vector heuristic selector.
     * @param heuristicNames The names of the heuristics to be used by the resulting vector heuristic selector.          
     * @param populationSize The number of individuals in the population.
     * @param nbCycles The number of cycles the genetic algorithm will run.
     * @param crossoverRate The crossover rate to be used by the genetic algorithm.
     * @param mutationRate The mutation rate to be used by the genetic algorithm.
     * @param selectionOperator The selection operator to be used by the genetic algorithm.
     * @param evaluationFunction The evaluation function to be used by the genetic algorithm.     
     * @param type The type of the genetic algorithm to use. 
     * @param printMode A flag to indicate whether or not the progress of the genetic algorithm should be printed on screen.
     * @param stopValue The desired objective value to stop the evolutionary process.
     * @param seed The seed to initialize the random number generator.
     * @return The heuristic selector resulting from running the framework.
     */
    public static Individual run(String[] featureNames, String[] heuristicNames, int populationSize, int nbCycles, double crossoverRate, double mutationRate, SelectionOperator selectionOperator, EvaluationFunction evaluationFunction, Type type, boolean printMode, double stopValue, long seed) {
        int nbRules, nbBits;
        Random random;
        Individual[] population;
        GeneticAlgorithm geneticAlgorithm;               
        random = new Random(seed);
        geneticAlgorithm = new GeneticAlgorithm(evaluationFunction, selectionOperator);        
        /*
         * Generates the initial population.
         */
        DistanceBasedHeuristicSelectorIndividual.setFeatureNames(featureNames);
        DistanceBasedHeuristicSelectorIndividual.setHeuristicNames(heuristicNames);
        nbRules = 5;
        nbBits = 10;
        population = new Individual[populationSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = new DistanceBasedHeuristicSelectorIndividual(nbRules, nbBits, random.nextLong());            
        }        
        /*
         * Runs the genetic algorithm process.
         */
        return geneticAlgorithm.run(population, nbCycles, crossoverRate, mutationRate, type, printMode, stopValue); 
    }
    
}
