package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.util.JRandom;

import java.util.LinkedList;

import static com.github.jotask.neat.jneat.util.Ref.CROSSOVER;

/**
 * Specie
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Specie implements Json.Serializable{

    private final LinkedList<Genome> genomes;
    public double topFitness;
    public double averageFitness;
    public int staleness;

    public Specie() {
        this.genomes = new LinkedList<Genome>();
        this.topFitness = 0.0;
        this.averageFitness = 0.0;
        this.staleness = 0;
    }

    public Genome breedChild() {
        final Genome child;
        if (JRandom.random() < CROSSOVER) {
            final Genome g1 = genomes.get(JRandom.randomIndex(genomes));
            final Genome g2 = genomes.get(JRandom.randomIndex(genomes));
            child = crossover(g1, g2);
        } else {
            final Genome gen = genomes.get(JRandom.randomIndex(genomes));
            child = new Genome(gen);
        }
        child.mutate();
        return child;
    }

    public void calculateAverageFitness() {
        double total = 0.0;
        for (final Genome genome : genomes)
            total += genome.globalRank;
        averageFitness = total / genomes.size();
    }

    private Genome crossover(Genome g1, Genome g2) {
        if (g2.fitness > g1.fitness) {
            final Genome tmp = g1;
            g1 = g2;
            g2 = tmp;
        }

        final Genome child = new Genome();
        outerloop: for (final Synapse gene1 : g1.getGenes()) {
            for (final Synapse gene2 : g2.getGenes())
                if (gene1.getInnovation() == gene2.getInnovation())
                    if (JRandom.nextBoolean() && gene2.isEnabled()) {
                        child.getGenes().add(new Synapse(gene2));
                        continue outerloop;
                    } else
                        break;
            child.getGenes().add(new Synapse(gene1));
        }

        child.maxNeuron = Math.max(g1.maxNeuron, g2.maxNeuron);

        child.step_size = g1.step_size;

        return child;
    }

    public LinkedList<Genome> getGenomes() { return genomes; }

    @Override
    public void write(Json json) {
        json.writeValue("topFitness", this.topFitness);
        json.writeValue("averageFitness", this.averageFitness);
        json.writeValue("staleness", this.staleness);
//        json.writeValue("genome", this.genome);
    }

    @Override
    public void read(Json json, JsonValue data) {
        this.topFitness = data.getDouble("topFitness");
        this.averageFitness = data.getDouble("averageFitness");
        this.staleness = data.getInt("staleness");
//        this.genome = json.readValue(Genome.class, data.get("genome"));
    }
}
