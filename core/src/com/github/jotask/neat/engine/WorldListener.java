package com.github.jotask.neat.engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.engine.entity.Food;
import com.github.jotask.neat.engine.entity.Radar;

/**
 * WorldController
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class WorldListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Object a = contact.getFixtureA().getUserData();
        Object b = contact.getFixtureB().getUserData();

        if(a == null || b == null)
            return;

        if(a instanceof Enemy && b instanceof Enemy)
            return;

        if((a instanceof Radar && b instanceof Enemy) || (b instanceof Radar && a instanceof Enemy))
            return;

        if(a instanceof Food && b instanceof Food)
            return;

        if(a instanceof Radar && b instanceof Radar)
            return;

        Enemy enemy = null;
        Food food = null;
        Radar radar = null;

        if(a instanceof Enemy){
            enemy = (Enemy) a;
        }else if(b instanceof Enemy){
            enemy = (Enemy) b;
        }
        if(a instanceof Food){
            food = (Food) a;
        }else if(b instanceof Food){
            food = (Food) b;
        }
        if(a instanceof Radar){
            radar = (Radar) a;
        }else if(b instanceof Radar){
            radar = (Radar) b;
        }

        if(radar != null && food != null){
            radar.inRange(food);
            return;
        }else if(enemy != null && food != null){
            enemy.eat(food);
            return;
        }else {
            System.out.println("unknown collision: a:" + a.getClass().getSimpleName() + " b: " + b.getClass().getSimpleName());
        }

    }

    @Override
    public void endContact(Contact contact) {

        Object a = contact.getFixtureA().getUserData();
        Object b = contact.getFixtureB().getUserData();

        if(a == null || b == null)
            return;


        if(a instanceof Enemy || b instanceof Enemy)
            return;

        if(a instanceof Food && b instanceof Food)
            return;

        if(a instanceof Radar && b instanceof Radar)
            return;

        Food food = null;
        Radar radar = null;

        if(a instanceof Food){
            food = (Food) a;
        }else if(b instanceof Food){
            food = (Food) b;
        }
        if(a instanceof Radar){
            radar = (Radar) a;
        }else if(b instanceof Radar){
            radar = (Radar) b;
        }

        if(radar != null && food != null){
            radar.outRange(food);
            return;
        }else {
            System.out.println("unknown collision endContact: a:" + a.getClass().getSimpleName() + " b: " + b.getClass().getSimpleName());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
