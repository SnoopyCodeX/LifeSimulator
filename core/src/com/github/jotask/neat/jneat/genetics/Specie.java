package com.github.jotask.neat.jneat.genetics;

/**
 * Specie
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Specie {

    public Genome genome;
    public double topFitness;
    public double averageFitness;
    public int stanles;

    public Specie(final Genome genome) {
        this.genome = genome;
        this.topFitness = 0.0;
        this.averageFitness = 0.0;
        this.stanles = 0;
    }

    public void calculateAverageFitness() {
        double total = 0.0;
        total += genome.globalRank;
        this.averageFitness = total;
    }

}
