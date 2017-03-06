package com.github.jotask.neat.jneat.fitness;

import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * Fitness
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public interface Fitness {

    double evaluate(final NeatEnemy e);

    void reset();

}
