package com.github.jotask.neat.jneat.fitness;

import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * BasicFitness
 *
 * Basic fitness function for the environment
 * The entity get evaluated by penalization.
 * the penalization is the distance between an entity to the player.
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
    public void update() {
        this.ticks++;
    }

    @Override
    public double evaluate(NeatEnemy e) {
        return ticks - (e.getScore() * 1.5);
    }

    @Override
    public void reset() {
        this.ticks = 0;
    }

}
