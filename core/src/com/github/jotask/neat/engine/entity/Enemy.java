package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.Timer;
import com.github.jotask.neat.engine.controller.EnemyController;
import com.github.jotask.neat.engine.radar.RadarEnemy;

/**
 * Enemy
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Enemy extends Entity{

    private float INIT_TIME = 1f;

    private final EnemyController controller;

    public final Vector2 velocity;

    public final float SPEED = 10;

    private final Timer timer;

    private final RadarEnemy radar;

    public Enemy(final Body body, final RadarEnemy radar) {
        super(body);
        this.radar = radar;
        this.controller = new EnemyController(this);
        this.velocity = new Vector2();
        this.timer = new Timer(INIT_TIME);
    }

    @Override
    public void input() { }

    @Override
    public void update() {

        this.getBody().applyForceToCenter(this.velocity.scl(SPEED), true);

        velocity.setZero();
        if(timer.isPassed()){
            this.die = true;
        }

    }

    public void eat(final Food food){
        float getValue = food.getValue();
        timer.add(getValue);
        food.eaten();
    }

    @Override
    public void debug(final ShapeRenderer sr){
        sr.setColor(Color.RED);
        sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
    }

    public EnemyController getController() { return controller; }
    public RadarEnemy getRadar() { return radar; }

}
