package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.engine.entity.Food;
import com.github.jotask.neat.engine.radar.RadarEnemy;

/**
 * NeatEnemy
 *
 * @author Jose Vives Iznardo
 * @since 19/02/2017
 */
public class NeatEnemy extends Enemy {

    private final Genome genome;
    private final Species species;

    public NeatEnemy(final Body body, final RadarEnemy radar, final Genome genome, final Species species) {
        super(body, radar);
        this.genome = genome;
        genome.generateNetwork();
        this.species = species;
    }

    public Genome getGenome() { return genome; }
    public Species getSpecies() { return species; }

    public double[] getInput(){
        final double[] inputs = new double[Pool.INPUTS];
        inputs[0] = this.getBody().getPosition().x;
        inputs[1] = this.getBody().getPosition().y;
        Food food = getRadar().getClosest();
        // true = 1 and false = 0
        inputs[2] = (food == null) ? 1 : 0;
        if(food != null) {
            inputs[3] = food.getBody().getPosition().y;
            inputs[4] = food.getBody().getPosition().y;
        }else{
            inputs[3] = 0;
            inputs[4] = 0;
        }
        return inputs;
    }

    public void setOutput(double[] output) {
        if(threshold(output[0])) getController().left();
        if(threshold(output[1])) getController().right();
        if(threshold(output[2])) getController().up();
        if(threshold(output[3])) getController().down();
    }

    private boolean threshold(double value){ return (value > JNeat.THRESHOLD); }

    @Override
    public void debug(ShapeRenderer sr) {
        super.debug(sr);

//        float x = this.getBody().getPosition().x;
//        float y = this.getBody().getPosition().y;
//
//        float width = 4f;
//        float height = .4f;
//
//        float off = .1f;
//
//        float life = 0;
//
//        sr.setColor(Color.BLACK);
//        sr.set(ShapeRenderer.ShapeType.Filled);
//        sr.rect(x - (width / 2f) / 2f, y, width / 2f, height);
//
//        sr.setColor(Color.RED);
//        sr.set(ShapeRenderer.ShapeType.Filled);
//        sr.rect((x - (width / 2f) / 2f) + off, y + off, (life) - off * 2, height - off * 2);

    }
}
