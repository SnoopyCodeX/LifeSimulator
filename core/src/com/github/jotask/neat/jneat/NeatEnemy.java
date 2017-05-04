package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.controller.EnemyController;
import com.github.jotask.neat.engine.controller.WeaponController;
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.engine.weapon.Weapon;
import com.github.jotask.neat.jneat.genetics.Genome;
import com.github.jotask.neat.jneat.network.Network;
import com.github.jotask.neat.jneat.util.Constants;
import com.github.jotask.neat.util.JRandom;

/**
 * NeatEnemy
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class NeatEnemy extends Enemy{

    private EnemyController controller;
    private WeaponController weaponController;

    private boolean disabled;

    private Genome genome;

    private Vector2 v;

    private Network network;

    private final double THRESHOLD;

    public boolean isBest;

    public NeatEnemy(Body body, final double THRESHOLD) {
        super(body);
        this.THRESHOLD = THRESHOLD;
        this.v = new Vector2();
        this.controller = new EnemyController(this);
        this.disable();
    }


    public void disable(){
        this.genome = null;
        this.network = null;
        this.getBody().setLinearVelocity(0,0);
        this.getBody().setAngularVelocity(0f);
        this.setPosition(Vector2.Zero);
        this.getBody().setActive(false);
        this.disabled = true;
        this.isBest = false;
        this.hits = 0;
    }

    public void activate(final Genome genome){
        if(this.genome != null)
            throw new RuntimeException("Species is not null");

        this.genome = genome;
        this.setPosition(JRandom.randomPosition());
        this.getBody().setActive(true);
        this.disabled = false;
        this.network = new Network(this.genome.getGenes());
        this.hits = 0;

    }

    public float getScore() {
        final Vector2 e = this.getBody().getPosition();
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        return e.dst(p);
    }

    public Genome getGenome() { return genome; }

    public boolean isDisabled() { return disabled; }

    private void setPosition(final Vector2 p){
        this.getBody().setTransform(p, this.getBody().getAngle());
    }

    @Override
    public void update() {
        super.update();
    }

    private double[] getInputs() {
        final double[] inputs = new double[Constants.INPUTS];
        inputs[Constants.Inputs.enemy_x.ordinal()] = this.getBody().getPosition().x;
        inputs[Constants.Inputs.enemy_y.ordinal()] = this.getBody().getPosition().y;
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        inputs[Constants.Inputs.player_x.ordinal()] = p.x;
        inputs[Constants.Inputs.player_y.ordinal()] = p.y;
        inputs[Constants.Inputs.bias.ordinal()] = 1.0d;
        return inputs;
    }

    private void setOutput(final double[] output) {
        if((output[Constants.Outputs.left.ordinal()]) > THRESHOLD) {
            this.controller.left();
        }
        if(output[Constants.Outputs.right.ordinal()] > THRESHOLD) {
            this.controller.right();
        }
        if(output[Constants.Outputs.up.ordinal()] > THRESHOLD) {
            this.controller.up();
        }
        if(output[Constants.Outputs.down.ordinal()] > THRESHOLD) {
            this.controller.down();
        }
        if(output[Constants.Outputs.w_left.ordinal()] > THRESHOLD) {
            this.weaponController.left();
        }
        if(output[Constants.Outputs.w_right.ordinal()] > THRESHOLD) {
            this.weaponController.right();
        }
        if(output[Constants.Outputs.w_up.ordinal()] > THRESHOLD) {
            this.weaponController.up();
        }
        if(output[Constants.Outputs.w_down.ordinal()] > THRESHOLD) {
            this.weaponController.down();
        }

        v.set(this.velocity);

    }

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

    public void evaluateNetwork() {
        final double[] input = this.getInputs();
        final double[] output = this.network.evaluate(input);
        this.setOutput(output);
    }

    public Network getNetwork() { return network; }

    @Override
    public void equip(Weapon weapon) {
        super.equip(weapon);
        this.weapon.equip(this);
        this.weaponController = new WeaponController(this.weapon);
    }
}
