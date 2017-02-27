package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * JNeat
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class JNeat {

    public static final float THRESHOLD = .75f;

    final Neat neat;

    private int ticks;

    public static final Random random = new Random();

    private final Population pool;

    private NeatEnemy best;

    private List<NeatEnemy> entities;

    private int alive;

    public JNeat(final Neat neat) {
        this.neat = neat;
        this.entities = new ArrayList<NeatEnemy>();
        this.ticks = 0;
        this.pool = new Population();
        init();
    }

    public void init(){
        this.entities.clear();
        this.alive = 0;
        for(final Species species: pool.species){
            for(final Genome genome: species.genomes){
                genome.generateNetwork();
                NeatEnemy entity = neat.getFactory().getNeatEnemy(genome);
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

        this.setBest(entities.get(0));

        boolean allDead = true;

        alive = entities.size();

        for (final NeatEnemy entities : entities) {

            if (entities.isDie()) {
                alive--;
                continue;
            }

            allDead = false;

            // TODO improve fitness
            double fitness = ticks++ - entities.getScore() * 1.5;
            fitness = fitness == 0.0 ? -1.0 : fitness;

            entities.getGenome().fitness = fitness;
            if (fitness > pool.maxFitness)
                pool.maxFitness = fitness;

            if (fitness > best.getGenome().fitness)
                this.setBest(entities);
        }

        if (allDead) {
            pool.newGeneration();
            init();
            Neat.get().getPlayer().respawn();
        }

    }

    private void setBest(final NeatEnemy e){

        if(best!= null)
            best.isBest = false;

        best = e;

        best.isBest = true;

    }

    public void render(final SpriteBatch sb, final ShapeRenderer sr){

        best.drawNetwork(neat.getCamera(), sb, sr);

    }

    public void dispose(){

    }

    public int getAlive(){ return this.alive; }

    public int getGeneration() { return this.pool.generation; }

}
