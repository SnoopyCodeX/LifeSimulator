package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;

import java.util.*;

import static com.github.jotask.neat.jneat.Pool.INPUTS;
import static com.github.jotask.neat.jneat.Pool.OUTPUTS;

/**
 * JNeat
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class JNeat {

    public static final float THRESHOLD = .5f;

    final Neat neat;

    private int ticks;

    public static final Random random = new Random();

    private final Pool pool;

    private NeatEnemy best;

    private List<NeatEnemy> entities;

    private int alive;

    public JNeat(final Neat neat) {
        this.neat = neat;
        this.entities = new ArrayList<NeatEnemy>();
        this.ticks = 0;
        this.pool = new Pool();
        this.pool.initializePool();
        init();
    }

    public void init(){
        this.entities.clear();
        this.alive = 0;
        for(final Species species: pool.species){
            for(final Genome genome: species.genomes){
                genome.generateNetwork();
                NeatEnemy entity = neat.getFactory().getNeatEnemy(genome, species);
                this.entities.add(entity);
            }
        }
    }

    public void evaluate(){
        for (final NeatEnemy entities : entities) {

            if (entities.isDie())
                continue;

            final double[] input = entities.getInput();
            final double[] output = entities.getGenome().evaluateNetwork(input);
            entities.setOutput(output);

        }
    }

    public void learn(){

        best = entities.get(0);
        boolean allDead = true;

        alive = entities.size();

        for (final NeatEnemy entities : entities) {

            if (entities.isDie()) {
                alive--;
                continue;
            }

            allDead = false;

            // TODO improve fitness
            double fitness = ticks++ - entities.score * 1.5;
            fitness = fitness == 0.0 ? -1.0 : fitness;

            entities.getGenome().fitness = fitness;
            if (fitness > pool.maxFitness)
                pool.maxFitness = fitness;

            if (fitness > best.getGenome().fitness)
                best = entities;
        }

        if (allDead) {
            pool.newGeneration();
            init();
        }
    }

    public void render(final SpriteBatch sb, final ShapeRenderer sr){

        final float minX;
        final float maxX;

        final float xM;
        final float yM;

        {
            final Camera c = neat.getCamera();
            minX = c.position.x - c.viewportWidth / 2f;
            maxX = c.position.x + c.viewportWidth / 2f;

            xM = c.position.x;
            yM = c.position.y;
        }

        float yInput = -(INPUTS * Cell.SIZE) / 2f;
        float yOutput = -(OUTPUTS * Cell.SIZE) / 2f;

        final Map<Integer, Cell> graph = new HashMap<Integer, Cell>();
        for(final Map.Entry<Integer, Neuron> entry : best.getGenome().network.entrySet()){
            final int i = entry.getKey();
            final Neuron neuron = entry.getValue();

            if(neuron.type == Neuron.Type.INPUT){
                float x = minX;
                float y = yInput;
                yInput += Cell.SIZE;
                graph.put(i, new Cell(x, y, neuron.value));
            }else if(neuron.type == Neuron.Type.OUTPUT){
                float x = maxX - Cell.SIZE;
                float y = yOutput;
                yOutput += Cell.SIZE;
                graph.put(i, new Cell(x, y, neuron.value));
            }else if(neuron.type == Neuron.Type.HIDDEN){
                float x = (minX + maxX) / 2f;
                float y = yM;
                graph.put(i, new Cell(x, y, neuron.value));
            }else{
                System.out.println("Unknown neuron type ");
            }
        }

        for(final Synapse gene: best.getGenome().genes){
            final Cell c1 = graph.get(gene.input);
            final Cell c2 = graph.get(gene.output);
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

        for (final Synapse gene : best.getGenome().genes) {

            if (gene.enabled) {
                final Cell c1 = graph.get(gene.input);
                final Cell c2 = graph.get(gene.output);
                final float value = (float) Math.abs(Neuron.sigmoid(gene.weight));
                final Color color;
                if (Neuron.sigmoid(gene.weight) > 0.0) {
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

    private static class Cell {

        private float x;
        private float y;
        private final double value;

        static float SIZE = 1f;

        public Cell(final float x, final float y, final double value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public void render(final SpriteBatch sb){

        }

        public void debug(final ShapeRenderer sr){
            final double value = this.value;
            final Color color;
            if(value > 0.0){
                color = Color.GREEN;
            }else{
                color = Color.RED;
            }
            sr.setColor(Color.YELLOW);
            sr.set(ShapeRenderer.ShapeType.Filled);
            sr.rect(this.x, this.y, SIZE, SIZE);

            sr.setColor(Color.BLACK);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.rect(this.x, this.y, SIZE, SIZE);

        }

    }

    public void dispose(){

    }

    public int getAlive(){ return this.alive; }

}
