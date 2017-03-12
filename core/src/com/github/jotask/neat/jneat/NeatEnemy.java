package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.jneat.genetics.Genome;
import com.github.jotask.neat.jneat.genetics.Specie;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.jneat.util.Util;
import com.github.jotask.neat.util.JRandom;

/**
 * NeatEnemy
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class NeatEnemy extends Enemy{

    private boolean disabled;

    private Specie specie;

    private Vector2 v;

    public boolean isBest;

    public NeatEnemy(Body body) {
        super(body);
        this.v = new Vector2();
        this.disable();

    }


    public void disable(){
        this.specie = null;
        this.getBody().setLinearVelocity(0,0);
        this.getBody().setAngularVelocity(0f);
        this.setPosition(Vector2.Zero);
        this.getBody().setActive(false);
        this.disabled = true;
        this.isBest = false;
    }

    public void activate(final Specie species){
        if(this.specie != null)
            throw new RuntimeException("Species is not null");

        this.specie = species;
        this.setPosition(JRandom.randomPosition());
        this.getBody().setActive(true);
        this.disabled = false;

    }

    public float getScore() {
        final Vector2 e = this.getBody().getPosition();
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        return e.dst2(p);
    }

    public Specie getSpecies() { return specie; }

    public boolean isDisabled() { return disabled; }

    private void setPosition(final Vector2 p){
        this.getBody().setTransform(p, this.getBody().getAngle());
    }

    @Override
    public void update() {
        super.update();
//        this.clearForces();
    }

    private void clearForces(){
        this.getBody().setLinearVelocity(0,0);
        this.getBody().setAngularVelocity(0);
    }

    public double[] getInputs() {
        final double[] inputs = new double[Ref.INPUTS];
        inputs[0] = this.getBody().getPosition().x;
        inputs[1] = this.getBody().getPosition().y;
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        inputs[2] = p.x;
        inputs[3] = p.y;
        inputs[4] = 1.0d;
        return inputs;
    }

    public void setOutput(final double[] output) {
        if(Util.threshold(output[Ref.Outputs.left.ordinal()])) {
            getController().left();
        }
        if(Util.threshold(output[Ref.Outputs.right.ordinal()])) {
            getController().right();
        }
        if(Util.threshold(output[Ref.Outputs.up.ordinal()])) {
            getController().up();
        }
        if(Util.threshold(output[Ref.Outputs.down.ordinal()])) {
            getController().down();
        }

        v.set(this.velocity);

    }

    public Genome getGenome(){ return this.specie.genome; }

    @Override
    public void debug(ShapeRenderer sr) {

        if(this.isDisabled())
            return;

        if(isBest){
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.LIME);
            sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);
        }else {
            super.debug(sr);
        }

        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        float x = getBody().getPosition().x;
        float y = getBody().getPosition().y;
        float w = x + v.x;
        float h = y + v.y;
        sr.line(x, y, w, h);

    }

}
