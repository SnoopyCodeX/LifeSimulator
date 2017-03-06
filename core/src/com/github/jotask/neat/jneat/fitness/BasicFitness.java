package com.github.jotask.neat.jneat.fitness;

import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * BasicFitness
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class BasicFitness implements Fitness {

    private int ticks = 0;

    @Override
    public double evaluate(final NeatEnemy e) {
        return (ticks++) - e.getScore() * 1.5;
    }

    @Override
    public void reset() {
        ticks = 0;
    }

}
