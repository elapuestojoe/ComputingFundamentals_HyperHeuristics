package HERMES;

import HERMES.Exceptions.NoSuchFeatureException;

/**
 * Defines the operations that all the feature managers supported by the system must implement.
 * <p>
 * @author Jose Carlos Ortiz Bayliss (jcobayliss@gmail.com)
 * @version 2.0
 */
public interface FeatureManager {
    
    /**
     * Returns the current value of a given feature.
     * <p>
     * @param feature The name of the feature to return.
     * @return The current value of a given feature.
     * @throws NoSuchFeatureException
     */
    public double getFeature(String feature)  throws NoSuchFeatureException;

}
