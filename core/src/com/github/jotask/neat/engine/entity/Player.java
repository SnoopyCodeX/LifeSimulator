package com.github.jotask.neat.engine.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.github.jotask.neat.engine.controller.PlayerController;
import com.github.jotask.neat.util.JRandom;

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

    public Player(final Body body) {
        super(body);
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
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
    }

    public void respawn(){
        Vector2 p = JRandom.randomPositionPlayer();
        this.getBody().setTransform(p, this.getBody().getAngle());
        this.clearForces();
    }

    public void explode(){
        explode(100, 20f, 1000f, this.getBody().getPosition().x, this.getBody().getPosition().y);
    }

    private void applyBlastImpusle(final Body body, final Vector2 blastCenter, final Vector2 applyPoint, float blastPower){
        Vector2 blastDir = applyPoint.cpy().sub(blastCenter);
        float distance = blastDir.len();
        if(distance == 0) return;

        float invDistance = 1f / distance;
        float impulseMag = Math.min(blastPower * invDistance, blastPower * 0.5f); //Not physically correct
        body.applyLinearImpulse(blastDir.nor().scl(impulseMag), applyPoint, true);
    }

    private void explode(final int numRays, float blastRadius, final float blastPower, float posX, float posY) {
        final Vector2 center = new Vector2(posX, posY);
        Vector2 rayDir = new Vector2();
        Vector2 rayEnd = new Vector2();

        for(int i = 0; i < numRays; i++) {
            float angle = (i / (float) numRays) * 360 * MathUtils.degreesToRadians;
            rayDir.set(MathUtils.sin(angle), MathUtils.cos(angle));
            rayEnd.set(center.x + blastRadius * rayDir.x, center.y + blastRadius * rayDir.y);

            RayCastCallback callback = new RayCastCallback() {

                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    applyBlastImpusle(fixture.getBody(), center, point, blastPower / (float)numRays); /* call our method we created earlier */
                    return 0;
                }
            };
            this.getBody().getWorld().rayCast(callback, center, rayEnd);
        }
    }


}
