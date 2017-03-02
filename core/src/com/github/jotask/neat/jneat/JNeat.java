package com.github.jotask.neat.jneat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.Util;
import com.github.jotask.neat.jneat.fitness.BasicFitness;
import com.github.jotask.neat.jneat.fitness.Fitness;
import com.github.jotask.neat.jneat.gui.Data;

import java.util.ArrayList;
import java.util.List;

import static com.github.jotask.neat.jneat.Constants.FILE;

/**
 * JNeat
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class JNeat {

    final Neat neat;

    public Population pool;

    private Data data;

    private Fitness fitness;

    private NeatEnemy best;

    private List<NeatEnemy> entities;

    private int alive;

    public JNeat(final Neat neat) {
        this.neat = neat;
        this.entities = new ArrayList<NeatEnemy>();

        this.data = new Data(neat.getCamera());

        this.fitness = new BasicFitness();

        this.pool = new Population();

        if(!load()){
            this.pool.init();
        }

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

    public void learn() {

        this.setBest(entities.get(0));

        boolean allDead = true;

        alive = entities.size();

        for (final NeatEnemy e : entities) {

            if (e.isDie()) {
                alive--;
                continue;
            }

            allDead = false;

            // TODO improve fitness
            double fitness = this.fitness.evaluate(e);

            fitness = (fitness == 0.0) ? -1.0 : fitness;

            e.getGenome().fitness = fitness;

            if (fitness > pool.maxFitness) {
                pool.maxFitness = fitness;
            }

            if (fitness > best.getGenome().fitness){
                this.setBest(e);
            }

        }

        if (allDead) {
            data.addFitness(pool.generation, best.getGenome().fitness);
            save();
            pool.newGeneration();
            init();
            Neat.get().getPlayer().respawn();
            fitness.reset();
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

    public void dispose(){ }

    public int getAlive(){ return this.alive; }

    public int getGeneration() { return this.pool.generation; }

    public void save(){

        Json json = new Json(JsonWriter.OutputType.json);

        final String s = json.prettyPrint(this.pool);
        final String ss = json.toJson(this.pool);

//        final FileHandle internal = Gdx.files.local(file);
//        internal.writeString(s, false);
//
//        final FileHandle intern = Gdx.files.local("data/genome.min.json");
//        intern.writeString(ss, false);
    }

    public boolean load(){
        final FileHandle internal = Gdx.files.local(FILE);
        if(!internal.exists()){
            return false;
        }else{
            return false;
        }

//        final String s = internal.readString();
//
//        Json json = new Json(JsonWriter.OutputType.json);
//        Genome g = json.fromJson(Genome.class, s);
//        return true;
    }

    public String getMaxFitness(){ return String.valueOf(Util.cutDecimals(this.pool.maxFitness, 2)); }

    public Data getData() { return data; }

}
