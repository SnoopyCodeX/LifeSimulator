package com.github.jotask.neat.jneat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import static com.github.jotask.neat.engine.JRandom.random;

public class Species implements Json.Serializable{

    public final List<Genome> genomes = new ArrayList<Genome>();
    public double topFitness = 0.0;
    public double averageFitness = 0.0;
    public int staleness = 0;

    public Genome breedChild() {
        final Genome child;
        if (random.nextDouble() < Constants.CROSSOVER) {
            final Genome g1 = genomes.get(random.nextInt(genomes.size()));
            final Genome g2 = genomes.get(random.nextInt(genomes.size()));
            child = crossover(g1, g2);
        } else {
            child = genomes.get(random.nextInt(genomes.size())).clone();
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

    public Genome crossover(Genome g1, Genome g2) {

        if (g2.fitness > g1.fitness) {
            final Genome tmp = g1;
            g1 = g2;
            g2 = tmp;
        }

        final Genome child = new Genome();
        outerLoop: for (final Synapse gene1 : g1.genes) {
            for (final Synapse gene2 : g2.genes)
                if (gene1.innovation == gene2.innovation)
                    if (random.nextBoolean() && gene2.enabled) {
                        child.genes.add(gene2.clone());
                        continue outerLoop;
                    } else
                        break;
            child.genes.add(gene1.clone());
        }

//        child.maxNeuron = Math.max(g1.maxNeuron, g2.maxNeuron);

        for (int i = 0; i < 7; ++i)
            child.mutationRates[i] = g1.mutationRates[i];

        return child;
    }

    @Override
    public void write(Json json) {
        json.writeValue("topFitness", topFitness, Double.class);
        json.writeValue("averageFitness", averageFitness, Double.class);
        json.writeValue("staleness", staleness, Integer.class);
        json.writeArrayStart("genome");
        for(final Genome g: this.genomes){
            json.writeValue(g);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

}
