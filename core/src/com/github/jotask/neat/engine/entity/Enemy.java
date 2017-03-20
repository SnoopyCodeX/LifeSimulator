package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.controller.EnemyController;
import com.github.jotask.neat.engine.controller.WeaponController;
import com.github.jotask.neat.engine.weapon.Weapon;

/**
 * Enemy
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Enemy extends Entity{

    private final EnemyController controller;
    private final WeaponController weaponController;

    public final Vector2 velocity;

    public final float SPEED = 10;

    private final Weapon weapon;

    public Enemy(final Body body, final Weapon weapon) {
        super(body);
        this.weapon = weapon;
        this.controller = new EnemyController(this);
        this.velocity = new Vector2();
        this.weaponController = new WeaponController(weapon);
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

    protected EnemyController getController() { return this.controller; }
    protected WeaponController getWeaponController(){ return this.weaponController; }

}
