package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.config.Config;
import com.github.jotask.neat.jneat.Jota;
import com.github.jotask.neat.util.JRandom;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Specie
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Specie implements Json.Serializable{

    final float CROSSOVER;
    final float MUTATION;

    private final LinkedList<Genome> genomes;
    double topFitness;
    double averageFitness;
    int staleness;

    Specie() {
        final Config cfg = Jota.get().getConfig();
        CROSSOVER = new Float(cfg.get(Config.Property.CROSSOVER));
        MUTATION = new Float(cfg.get(Config.Property.MUTATION));
        this.genomes = new LinkedList<Genome>();
        this.topFitness = 0.0;
        this.averageFitness = 0.0;
        this.staleness = 0;
    }

    Genome breedChild() {
        final Genome child;
        if (JRandom.random() < CROSSOVER) {
            final Genome g1 = genomes.get(JRandom.randomIndex(genomes));
            final Genome g2 = genomes.get(JRandom.randomIndex(genomes));
            child = crossover(g1, g2);
        } else {
            final Genome genome = genomes.get(JRandom.randomIndex(genomes));
            child = new Genome(genome);
        }
        if(JRandom.random() < MUTATION) {
            child.mutate();
        }
        return child;
    }

    void calculateAverageFitness() {
        double total = 0.0;
        for (final Genome genome : genomes) {
            total += genome.globalRank;
        }
        averageFitness = total / genomes.size();
    }

    private Genome crossover(Genome mother, Genome father) {
        // Make sure that mother is the higher fitness genome
        if (father.fitness > mother.fitness) {
            final Genome tmp = mother;
            mother = father;
            father = tmp;
        }

        final Genome child = new Genome();

        HashMap<Integer, Synapse> innovations = new HashMap<Integer, Synapse>();
        for(final Synapse s: father.getGenes()){
            innovations.put(s.getInnovation(), s);
        }

        for(int i = 0; i < mother.getGenes().size(); i++){
            Synapse one = mother.getGenes().get(i);
            Synapse two = innovations.get(one.getInnovation());
            if(two != null){
                if(JRandom.nextBoolean() && two.isEnabled()){
                    child.getGenes().add(new Synapse(two));
                }else{
                    child.getGenes().add(new Synapse(one));
                }

            }
        }

        child.maxNeuron = Math.max(mother.maxNeuron, father.maxNeuron);

        child.step_size = mother.step_size;

        return child;
    }

    @Override
    public void write(Json json) {
        json.writeValue("topFitness", this.topFitness);
        json.writeValue("averageFitness", this.averageFitness);
        json.writeValue("staleness", this.staleness);
        json.writeValue("genomes", this.genomes);
    }

    @Override
    public void read(Json json, JsonValue data) {
        this.topFitness = data.getDouble("topFitness");
        this.averageFitness = data.getDouble("averageFitness");
        this.staleness = data.getInt("staleness");
        for (JsonValue v : data.get("genomes")) {
            Genome g = json.readValue(Genome.class, v);
            this.genomes.add(g);
        }
    }

    public LinkedList<Genome> getGenomes() { return genomes; }

}
