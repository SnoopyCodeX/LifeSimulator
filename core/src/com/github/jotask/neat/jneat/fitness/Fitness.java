package com.github.jotask.neat.jneat.fitness;

import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * Fitness
 *
 * Interface for a fitness function.
 *
 * A fitness function define how "fit" our how "good" the solution is with respect to the problem in cosideration
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public interface Fitness {

    /**
     * Update the fitness function
     */
    void update();

    /**
     * Evaluate with a fitness function
     *
     * A fitness functions needs to be fast to compute
     *
     * @param e
     *      Actual enemy to be evaluate by the provided function
     * @return
     *      the fitness value for this entity
     */
    double evaluate(final NeatEnemy e);

    /**
     * Reset the fitness function
     */
    void reset();

}
