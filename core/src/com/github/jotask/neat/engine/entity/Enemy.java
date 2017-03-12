package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.controller.EnemyController;

/**
 * Enemy
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Enemy extends Entity{

    private final EnemyController controller;

    public final Vector2 velocity;

    public final float SPEED = 10;

    public Enemy(final Body body) {
        super(body);
        this.controller = new EnemyController(this);
        this.velocity = new Vector2();
    }

    @Override
    public void input() { }

    @Override
    public void update() {
        this.getBody().applyForceToCenter(this.velocity.scl(SPEED), true);
        this.velocity.setZero();
    }

    @Override
    public void debug(final ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
    }

    public EnemyController getController() { return controller; }

}
