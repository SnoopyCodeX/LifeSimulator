package com.github.jotask.neat.jneat.fitness;

import com.badlogic.gdx.math.Vector2;
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

    private float movementRange = 1f;

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
        double vel = 0.0;
        final Vector2 v = e.getBody().getLinearVelocity();
        if( Math.abs(v.len()) > movementRange){
            vel = 5.0;
        }
        return ticks - (e.getScore() * 1.5f) + vel + (e.getHits() * 1.5f) ;
    }

    @Override
    public void reset() {
        this.ticks = 0;
    }

}
