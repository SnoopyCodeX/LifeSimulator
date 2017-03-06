package com.github.jotask.neat.jneat;

import com.github.jotask.neat.util.JRandom;

import java.util.LinkedList;

/**
 * Species
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Species {

    public LinkedList<Genome> genomes;

    public double topFitness;
    public double averageFitness;
    public int staleness = 0;

    public Species() {
        this.genomes = new LinkedList<Genome>();
        this.topFitness = 0.0;
        this.averageFitness = 0.0;
        this.staleness = 0;
    }

    public Genome breedChild() {
        final Genome child;
        if (JRandom.random() < Constants.CROSSOVER) {
            final Genome g1 = genomes.get(JRandom.random.nextInt(genomes.size()));
            final Genome g2 = genomes.get(JRandom.random.nextInt(genomes.size()));
            child = crossover(g1, g2);
        } else {
            child = new Genome(genomes.get(JRandom.random.nextInt(genomes.size())));
        }
        child.mutate();
        return child;
    }

    public void calculateAverageFitness() {
        double total = 0.0;
        for (final Genome genome : genomes) {
            total += genome.getGlobalRank();
        }
        averageFitness = total / genomes.size();
    }

    public Genome crossover(Genome g1, Genome g2) {

        if (g2.getFitness() > g1.getFitness()) {
            final Genome tmp = g1;
            g1 = g2;
            g2 = tmp;
        }

        final Genome child = new Genome();
        outerLoop: for (final Synapse gene1 : g1.getGenes()) {
            for (final Synapse gene2 : g2.getGenes()){
                if (gene1.getInnovation() == gene2.getInnovation()) {
                    if (JRandom.random.nextBoolean() && gene2.isEnabled()) {
                        child.getGenes().add(new Synapse(gene2));
                        continue outerLoop;
                    } else {
                        break;
                    }
                }
            }
            child.getGenes().add(new Synapse(gene1));
        }

        return child;
    }

    @Override
    protected Species clone() throws CloneNotSupportedException {
        throw new RuntimeException("Species clone");
    }

}
