package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.Timer;
import com.github.jotask.neat.jneat.fitness.BasicFitness;
import com.github.jotask.neat.jneat.fitness.Fitness;
import com.github.jotask.neat.jneat.gui.Gui;
import com.github.jotask.neat.jneat.gui.NetworkRenderer;
import com.github.jotask.neat.jneat.gui.Renderer;

/**
 * JNeat
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class JNeat implements Renderer{

    final Neat neat;

    final Fitness fitness;

    final Population population;

    final EnemyManager manager;

    NeatEnemy best;

    final NetworkRenderer renderer;

    final Timer timer;

    final Gui gui;

    public JNeat(final Neat neat) {
        this.neat = neat;
        this.manager = new EnemyManager();
        this.fitness = new BasicFitness();
        this.population = new Population();
        this.population.init();
        this.timer = new Timer(Constants.INIT_TIME);
        this.gui = new Gui(this);
        this.renderer = new NetworkRenderer(this, neat.getGui().getCamera());
        init();
    }

    public void init(){
        this.manager.clear();
        for(final Species specie: this.population.species){
            for(final Genome genome: specie.genomes){
                genome.generateNetwork();
                this.manager.spawn(specie, genome);
            }
        }
    }

    public void evaluate(){

        for(final NeatEnemy e: this.manager.getActive()){

            if(e.isDie() || e.isDisabled()){
                continue;
            }

            final double input[] = e.getInputs();
            final double output[] = e.getGenome().evaluateNetwork(input);
            e.setOutput(output);

        }
    }

    public void learn(){

        this.setBest(this.manager.getActive().getFirst());

        for(final NeatEnemy e: this.manager.getActive()){

            if(e.isDie() || e.isDisabled()){
                System.out.println("Bye entity in learn");
                continue;
            }

            double fitness = this.fitness.evaluate(e);

            if(fitness == 0.0){
                fitness = -1.0;
            }

            e.getGenome().setFitness(fitness);

            this.population.isMaxFitness(fitness);

            if(fitness > best.getGenome().getFitness()){
                this.setBest(e);
            }

        }

        if(timer.isPassed(true)){
            this.gui.getFitness().addFitness(this.population.generation, this.best.getGenome().getFitness());
            this.manager.clear();
            this.fitness.reset();
            this.population.newGeneration();
            this.init();
            Neat.get().getPlayer().respawn();
        }

    }

    public void dispose(){
        this.manager.dispose();
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

    private void setBest(final NeatEnemy e){
        if(this.best != null)
            this.best.isBest = false;
        this.best = e;
        this.best.isBest = true;
        this.renderer.createNetwork(e);
    }

    public Population getPopulation() { return population; }

    public NeatEnemy getBest() {
        return best;
    }

    public EnemyManager getManager() { return manager; }

}
