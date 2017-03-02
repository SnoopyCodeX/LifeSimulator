package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Food
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Food extends Entity{

    private static final float VALUE = 3f;

    public Food(Body body) {
        super(body);
    }

    public void eaten(){ this.die = true; }

    public float getValue() { return Food.VALUE; }

}
