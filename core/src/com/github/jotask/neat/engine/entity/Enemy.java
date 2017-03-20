package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.weapon.Weapon;

/**
 * Enemy
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Enemy extends Entity{

    public final Vector2 velocity;

    private final float SPEED = 10;

    protected Weapon weapon;

    protected int hits;

    public Enemy(final Body body) {
        super(body);
        this.velocity = new Vector2();
        this.hits = 0;
    }

    @Override
    public void input() { }

    @Override
    public void update() {
        this.getBody().applyForceToCenter(this.velocity.scl(SPEED), true);
        this.velocity.setZero();

        this.weapon.update(this.getBody());

    }

    @Override
    public void debug(final ShapeRenderer sr){

        if(this.weapon != null){
            this.weapon.render(sr);
        }

        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
    }

    public void equip(final Weapon weapon){
        this.weapon = weapon;
    }

    public int getHits() { return hits; }

    public void hit(){ this.hits++; }

}
