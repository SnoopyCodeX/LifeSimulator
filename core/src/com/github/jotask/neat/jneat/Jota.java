package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.Timer;
import com.github.jotask.neat.jneat.fitness.BasicFitness;
import com.github.jotask.neat.jneat.fitness.Fitness;
import com.github.jotask.neat.jneat.genetics.Population;
import com.github.jotask.neat.jneat.genetics.Specie;
import com.github.jotask.neat.jneat.gui.Gui;
import com.github.jotask.neat.jneat.gui.Renderer;
import com.github.jotask.neat.jneat.network.NetworkRenderer;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.util.Files;

/**
 * Jota
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Jota implements Renderer {

    private int ticks;

    private final JotaManager manager;

    private NeatEnemy best;

    private Gui gui;
    private NetworkRenderer renderer;

    private final Timer timer;
    private Population population;

    private final Fitness fitness;

    public Jota() {
        this.timer = new Timer(Ref.INIT_TIME);
        this.manager = new JotaManager();
        this.gui = new Gui(this);
        this.renderer = new NetworkRenderer(this);

        this.population = Files.load();

        this.fitness = new BasicFitness();
        initializeGame();
    }

    public void eval() {

        for (final NeatEnemy e : this.manager.getActive()) {

            if (e.isDisabled() || e.isDie())
                continue;

            final double[] input = e.getInputs();
            final double[] output = e.getGenome().getNetwork().evaluate(input);
            e.setOutput(output);

        }
    }

    public void initializeGame() {
        ticks = 0;
        best = null;
        this.manager.clear();
        for (final Specie specie : this.population.getSpecies()) {
            specie.genome.generateNetwork();
            this.manager.spawn(specie);
        }
        this.manager.moveDisabled();

        Files.save(this.population);

    }

    public void learn() {

        this.fitness.update();

        NeatEnemy b = this.manager.getActive().getFirst();

        for (final NeatEnemy e : this.manager.getActive()) {

            if (e.isDisabled() || e.isDie())
                continue;

            double fit = this.fitness.evaluate(e);
            fit = fit == 0.0 ? -1.0 : fit;

            e.getGenome().fitness = fit;
            if (fit > this.population.maxFitness)
                this.population.maxFitness = fit;

            if (fit > b.getGenome().fitness)
                b = e;
        }

        if(this.best != b){
            this.setBest(b);
        }

        if (timer.isPassed()) {
            this.gui.getFitness().addFitness(this.population.generation, this.best.getGenome().fitness);
            this.manager.clear();
            this.fitness.reset();
            this.population.newGeneration();
            initializeGame();
            Neat.get().getPlayer().respawn();
            this.timer.reset();
        }
    }

    private void setBest(NeatEnemy fp){
        if(this.best != null){
            this.best.isBest = false;
        }
        this.best = fp;
        this.best.isBest = true;
        this.renderer.createNetwork(fp);
    }

    public NeatEnemy getBest() {
        return best;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        this.gui.render(sb);
        this.renderer.render(sb);
        sb.end();
    }

    @Override
    public void debug(ShapeRenderer sr) {
        sr.begin();
        this.gui.debug(sr);
        this.renderer.debug(sr);
        sr.end();
    }

    public int getPopulation() { return this.population.getSpecies().size(); }

    public JotaManager getManager() { return manager; }

    public int getGenomes(){
        int count = 0;
        for(Specie s: population.getSpecies()){
            count++;
        }
        return count;
    }

    public Population getPop(){
        return this.population;
    }

    public void dispose(){
        this.manager.dispose();
    }

}
