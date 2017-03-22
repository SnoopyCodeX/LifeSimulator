package com.github.jotask.neat.jneat.fitness;

import com.badlogic.gdx.math.Vector2;
import com.github.jotask.neat.config.Config;
import com.github.jotask.neat.jneat.Jota;
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

    private final float PENALIZATION_DISTANCE;
    private final float PENALIZATION_VELOCITY;
    private final float PENALIZATION_HITS;

    private int ticks;

    public BasicFitness() {
        final Config cfg = Jota.get().getConfig();
        PENALIZATION_DISTANCE = new Float(cfg.get(Config.Property.PENALIZATION_DISTANCE));
        PENALIZATION_VELOCITY = new Float(cfg.get(Config.Property.PENALIZATION_VELOCITY));
        PENALIZATION_HITS = new Float(cfg.get(Config.Property.PENALIZATION_HITS));
        this.reset();
    }

    @Override
    public void update() {
        this.ticks++;
    }

    @Override
    public double evaluate(NeatEnemy e) {
        double vel = 0;
        final Vector2 v = e.getBody().getLinearVelocity();
        if( Math.abs(v.len()) > movementRange){
            vel = 1.0;
        }
        return ticks - (e.getScore() * PENALIZATION_DISTANCE) + (vel * PENALIZATION_VELOCITY) + (e.getHits() * PENALIZATION_HITS) ;
    }

    @Override
    public void reset() {
        this.ticks = 0;
    }

}
