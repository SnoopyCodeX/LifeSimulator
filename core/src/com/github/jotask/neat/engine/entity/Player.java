package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.JRandom;
import com.github.jotask.neat.engine.controller.PlayerController;
import com.github.jotask.neat.engine.radar.RadarPlayer;

/**
 * Player
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class Player extends Entity{

    private final PlayerController controller;

    public final Vector2 velocity;

    public final float SPEED = 10;

    private final RadarPlayer radar;

    public Player(final Body body, final RadarPlayer radar) {
        super(body);
        this.radar = radar;
        controller = new PlayerController(this);
        this.velocity = new Vector2();
    }

    @Override
    public void update() {

        controller.moveRandom();

        this.getBody().applyForceToCenter(this.velocity.scl(SPEED), true);
        velocity.setZero();
    }

    @Override
    public void debug(ShapeRenderer sr) {
        sr.setColor(Color.LIME);
        sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
    }

    public void respawn(){
        Vector2 p = JRandom.randomPosition();
        this.getBody().setTransform(p, this.getBody().getAngle());
    }

}
