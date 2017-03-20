package com.github.jotask.neat.engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.jotask.neat.engine.entity.Player;
import com.github.jotask.neat.engine.weapon.Sword;

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

        if(a == null ||b == null){
            return;
        }

        if((a instanceof Sword || b instanceof Sword) && (a instanceof Player ||b instanceof Player)){
            final Sword sword;
            if(a instanceof Sword){
                sword = (Sword) a;
            }else{
                sword = (Sword) b;
            }
            sword.getOwner().hit();
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
