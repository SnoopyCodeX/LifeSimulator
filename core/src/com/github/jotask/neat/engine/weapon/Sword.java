package com.github.jotask.neat.engine.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Swo
 *
 * @author Jose Vives Iznardo
 * @since 20/03/2017
 */
public class Sword extends Weapon {

    public static final Vector2 SIZE = new Vector2(1f, .25f);

    final Body body;

    final Color color;

    public Sword(final Body body) {
        this.body = body;
        this.color = Color.CYAN;
        this.color.a = .5f;
    }

    @Override
    public void update(final Body other) {
        this.body.setTransform(other.getPosition(), this.body.getAngle());
        isAttacking = attack();
        this.body.setAwake(isAttacking);
        if(isAttacking) {
            this.enableSword();
        }else{
            this.disableSword();
        }
        this.shotDirection.setZero();
    }

    public void enableSword(){
        final Shape s = this.body.getFixtureList().first().getShape();
        if(s instanceof PolygonShape){
            ((PolygonShape) s).setAsBox(SIZE.x, SIZE.y);
        }
    }

    public void disableSword(){
        final Shape s = this.body.getFixtureList().first().getShape();
        if(s instanceof PolygonShape){
            ((PolygonShape) s).setAsBox(0f, 0f);
        }else{
            System.err.println("Unknown");
        }
    }

    @Override
    public boolean attack() {
        if(this.shotDirection.x < 0){
            this.stab(180);
        }else if(this.shotDirection.x > 0){
            this.stab(-180);
        }else if(this.shotDirection.y < 0){
            this.stab(90);
        }else if(this.shotDirection.y > 0){
            this.stab(-90);
        }else{
            return false;
        }
        return true;
    }

    private void stab(final float angle){
        final Vector2 p = body.getPosition().cpy();
        if(angle == 180){
            p.x -= SIZE.x;
        }else if(angle == -180){
            p.x += SIZE.x;
        }else if(angle == 90){
            p.y += SIZE.x;
        }else if(angle == -90){
            p.y -= SIZE.x;
        }
        this.body.setTransform( p, (float) Math.toRadians(angle));
    }

    @Override
    public void render(final ShapeRenderer sr){

        if(!this.isAttacking())
           return;

        sr.set(ShapeRenderer.ShapeType.Filled);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        sr.setColor(this.color);
        final Vector2 p = this.body.getPosition();
        float angle = (float) Math.abs(Math.toDegrees(this.body.getAngle()));
        if(angle == 90){
            sr.rect(p.x - (SIZE.y), p.y - (SIZE.x), SIZE.y * 2f, SIZE.x * 2f);
        }else if(angle == 180){
            sr.rect(p.x - (SIZE.x), p.y - (SIZE.y), SIZE.x * 2f, SIZE.y * 2f);
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    public void dispose(){
        final World world = this.body.getWorld();
        world.destroyBody(this.body);
    }

}
