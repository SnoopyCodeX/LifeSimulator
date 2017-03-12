package com.github.jotask.neat.jneat.fitness;

import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * BasicFitness
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class BasicFitness implements Fitness {

    private int ticks;

    public BasicFitness() {
        this.reset();
    }

    @Override
    public double evaluate(NeatEnemy e) {
        return ++ticks - e.getScore() * 1.5;
    }

    @Override
    public void reset() {
        this.ticks = 0;
    }
}
