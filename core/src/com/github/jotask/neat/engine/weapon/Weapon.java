package com.github.jotask.neat.engine.weapon;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Weapon
 *
 * @author Jose Vives Iznardo
 * @since 20/03/2017
 */
public abstract class Weapon {

    public final Vector2 shotDirection;

    public Weapon() {
        this.shotDirection = new Vector2();
    }

    protected boolean isAttacking;

    public abstract boolean attack();

    public abstract void update(final Body body);

    public abstract void render(final ShapeRenderer sr);

    public abstract void dispose();

    public boolean isAttacking() { return isAttacking; }

}
