package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.jneat.neurons.Hidden;
import com.github.jotask.neat.jneat.neurons.Input;
import com.github.jotask.neat.jneat.neurons.Neuron;
import com.github.jotask.neat.jneat.neurons.Output;

import java.util.HashMap;
import java.util.Map;

import static com.github.jotask.neat.jneat.Constants.INPUTS;
import static com.github.jotask.neat.jneat.Constants.OUTPUTS;

/**
 * NeatEnemy
 *
 * @author Jose Vives Iznardo
 * @since 19/02/2017
 */
public class NeatEnemy extends Enemy {

    private final Genome genome;

    private Vector2 v;

    public boolean isBest;

    private float score;

    public NeatEnemy(final Body body, final Genome genome) {
        super(body);
        this.genome = genome;
        this.genome.generateNetwork();
        v = new Vector2();
        this.score = 0f;
    }

    public Genome getGenome() { return genome; }

    public double[] getInput(){
        final double[] inputs = new double[INPUTS];
        inputs[0] = this.getBody().getPosition().x;
        inputs[1] = this.getBody().getPosition().y;
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        inputs[2] = p.x;
        inputs[3] = p.y;
        return inputs;
    }

    public void setOutput(double[] output) {
        if(threshold(output[Output.OUTPUT.LEFT.ordinal()])){
            getController().left();
//            System.out.println("LEFT");
//            System.out.println("---");
        }
        if(threshold(output[Output.OUTPUT.RIGHT.ordinal()])) getController().right();
        if(threshold(output[Output.OUTPUT.UP.ordinal()])) getController().up();
        if(threshold(output[Output.OUTPUT.DOWN.ordinal()])) getController().down();

        v.set(this.velocity);

    }

    private boolean threshold(double value){ return (value > Constants.THRESHOLD); }

    @Override
    public void debug(ShapeRenderer sr) {
        if(!isBest) {
            super.debug(sr);
        }else {
            sr.setColor(Color.BLUE);
            sr.circle(getBody().getPosition().x, getBody().getPosition().y, .5f, 20);

            sr.set(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.BLACK);
            float x = getBody().getPosition().x;
            float y = getBody().getPosition().y;
            float w = x + v.x;
            float h = y + v.y;
            sr.line(x, y, w, h);

        }

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

    public void drawNetwork(final Camera c, SpriteBatch sb, ShapeRenderer sr){

        if(!isBest){
            System.out.println("isNotBest");
            return;
        }

        final float minX;
        final float maxX;

        final float yM;

        {
//            minX = c.position.x - c.viewportWidth / 2f;
//            maxX = c.position.x + c.viewportWidth / 2f;

            minX = -21f / 2f;
            maxX = 21f / 2f;

            yM = c.position.y;
        }

        float yInput = -(INPUTS * Cell.SIZE) / 2f;
        float yOutput = -(OUTPUTS * Cell.SIZE) / 2f;

        final Map<Integer, Cell> graph = new HashMap<Integer, Cell>();
        for(final Map.Entry<Integer, Neuron> entry : this.getGenome().network.entrySet()){

            final int i = entry.getKey();
            final Neuron neuron = entry.getValue();

            if(neuron instanceof Input){
                final float y = yInput;
                yInput += Cell.SIZE;
                graph.put(i, new Cell(i, minX, y, neuron.value, neuron.type));
            }else if(neuron instanceof Output){
                float x = maxX - Cell.SIZE;
                float y = yOutput;
                yOutput += Cell.SIZE;
                graph.put(i, new Cell(i, x, y, neuron.value, neuron.type));
            }else if(neuron instanceof Hidden){

                float x = ((minX + 30f) + (maxX - 30f)) / 2f;

                float j = 0;

                for(Synapse s: neuron.inputs){
                    Neuron a = this.genome.network.network.get(s.input);
                    Neuron b = this.genome.network.network.get(s.input);

                }

                float y = yM;
                graph.put(i, new Cell(i, x, y, neuron.value, neuron.type));
            }else{
                System.out.println("Unknown neuron type ");
            }
        }

        // FIXME sometimes a synapse link is null

        for(final Synapse gene: this.getGenome().genes){
            final Cell c1 = graph.get(gene.input);
            final Cell c2 = graph.get(gene.output);

            if(c1 == null || c2 == null){
                System.out.println("isNull");
                continue;
            }

            if (gene.input >= INPUTS + OUTPUTS) {
                c1.x = (int) (0.75 * c1.x + 0.25 * c2.x);
                if (c1.x >= c2.x) c1.x = c1.x - 60;
                if (c1.x < minX)  c1.x = minX;
                if (c1.x > maxX)  c1.x = maxX;
                c1.y = (int) (0.75 * c1.y + 0.25 * c2.y);
            }
            if (gene.output >= INPUTS + OUTPUTS) {
                c2.x = (int) (0.25 * c1.x + 0.75 * c2.x);
                if (c1.x >= c2.x) c2.x = c2.x + 60;
                if (c2.x < minX)  c2.x = minX;
                if (c2.x > maxX)  c2.x = maxX;
                c2.y = (int) (0.25 * c1.y + 0.75 * c2.y);
            }
        }

        sr.set(ShapeRenderer.ShapeType.Filled);

        for(Cell cell: graph.values()){
            cell.render(sb);
            cell.debug(sr);
        }

        for (final Synapse gene : this.getGenome().genes) {

            if (gene.enabled) {
                final Cell c1 = graph.get(gene.input);
                final Cell c2 = graph.get(gene.output);
                final float value = (float) Math.abs(Neuron.sigmoid(gene.weight));
                final Color color;
                if (value > 0.0) {
                    color = Color.GREEN;
                }else {
                    color = Color.RED;
                }
                sr.setColor(color);
                float off = Cell.SIZE / 2f;
                sr.line(c1.x + off, c1.y + off, c2.x + off, c2.y + off);
            }
        }
    }

    public float getScore() {
        final Vector2 e = this.getBody().getPosition();
        final Vector2 p = Neat.get().getPlayer().getBody().getPosition();
        score = e.dst2(p);
        return score;
    }
}
