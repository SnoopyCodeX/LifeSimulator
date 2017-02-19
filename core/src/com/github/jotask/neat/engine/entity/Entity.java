package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Entity
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public abstract class Entity {

    private final Body body;

    protected boolean die;

    public Entity(Body body) {
        this.body = body;
        this.body.setUserData(this);
    }

    public void input(){

    }

    public void update(){

    }

    public void render(SpriteBatch sb){

    }

    public void debug(ShapeRenderer sr){

    }

    public void die(){ }

    public boolean isDie() { return die; }

    public Body getBody() { return body; }
}
