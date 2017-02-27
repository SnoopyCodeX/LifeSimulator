package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Output;

import java.util.*;

import static com.github.jotask.neat.jneat.JNeat.random;

/**
 * NeatAlgorithm
 *
 * @author Jose Vives Iznardo
 * @since 15/02/2017
 */
public class Population {

    public static final int POPULATION    = 50;
    public static final int STALE_SPECIES = 15;
    public static final int INPUTS        = 4;
    public static final int OUTPUTS       = Output.OUTPUT.values().length;

    public static final double DELTA_DISJOINT  = 2.0;
    public static final double DELTA_WEIGHTS   = 0.4;
    public static final double DELTA_THRESHOLD = 1.0;

    public static final double CONN_MUTATION    = 0.25;
    public static final double LINK_MUTATION    = 2.0;
    public static final double BIAS_MUTATION    = 0.4;
    public static final double NODE_MUTATION    = 0.5;
    public static final double ENABLE_MUTATION  = 0.2;
    public static final double DISABLE_MUTATION = 0.4;
    public static final double STEP_SIZE        = 0.1;
    public static final double PERTURBATION     = 0.9;
    public static final double CROSSOVER        = 0.75;

    public final List<Species> species = new ArrayList<Species>();
    public int generation = 0;
    public static int innovation = OUTPUTS;
    public double maxFitness = 0.0;

    public Population(){
        for (int i = 0; i < POPULATION; ++i) {
            final Genome basic = new Genome();
            addToSpecies(basic);
            basic.maxNeuron = INPUTS;
            basic.mutate();
        }
//        check();
    }

    private void check(){
        for(Species s: species){
            for(Genome g: s.genomes){
                for(Synapse l: g.genes){
                    if(l.isOut()){
                        throw new RuntimeException("Two outputs neurons connected");
                    }
                }
            }
        }
    }

    public void addToSpecies(final Genome child) {

        for (final Species species : this.species)
            if (child.sameSpecies(species.genomes.get(0))) {
                species.genomes.add(child);
                return;
            }

        final Species childSpecies = new Species();
        childSpecies.genomes.add(child);
        species.add(childSpecies);

    }

    private void cullSpecies(final boolean cutToOne) {
        for (final Species species : this.species) {
            Collections.sort(species.genomes, new Comparator<Genome>() {

                @Override
                public int compare(final Genome o1, final Genome o2) {
                    final double cmp = o2.fitness - o1.fitness;
                    return cmp == 0.0 ? 0 : cmp > 0.0 ? 1 : -1;
                }
            });

            double remaining = Math.ceil(species.genomes.size() / 2.0);
            if (cutToOne)
                remaining = 1.0;

            while (species.genomes.size() > remaining)
                species.genomes.remove(species.genomes.size() - 1);
        }
    }

    void newGeneration() {
        cullSpecies(false);
        rankGlobally();
        removeStaleSpecies();
        rankGlobally();
        for (final Species species : this.species){
            species.calculateAverageFitness();
        }
        removeWeakSpecies();

        final double sum = totalAverageFitness();
        final List<Genome> children = new ArrayList<Genome>();
        for (final Species species : this.species) {
            final double breed = Math.floor(species.averageFitness / sum * POPULATION) - 1.0;
            for (int i = 0; i < breed; ++i)
                children.add(species.breedChild());
        }

        cullSpecies(true);

        while (children.size() + species.size() < POPULATION) {
            final Species species = this.species.get(random.nextInt(this.species.size()));
            children.add(species.breedChild());
        }

        for (final Genome child : children)
            addToSpecies(child);

        ++generation;

    }

    private void rankGlobally() {
        final List<Genome> global = new ArrayList<Genome>();
        for (final Species species : this.species)
            for (final Genome genome : species.genomes)
                global.add(genome);

        Collections.sort(global, new Comparator<Genome>() {

            @Override
            public int compare(final Genome o1, final Genome o2) {
                final double cmp = o1.fitness - o2.fitness;
                return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
            }
        });

        for (int i = 0; i < global.size(); ++i)
            global.get(i).globalRank = i;
    }

    private void removeStaleSpecies() {
        final List<Species> survived = new ArrayList<Species>();
        for (final Species species : this.species) {
            Collections.sort(species.genomes, new Comparator<Genome>() {

                @Override
                public int compare(final Genome o1, final Genome o2) {
                    final double cmp = o2.fitness - o1.fitness;
                    return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
                }
            });

            if (species.genomes.get(0).fitness > species.topFitness) {
                species.topFitness = species.genomes.get(0).fitness;
                species.staleness = 0;
            } else
                ++species.staleness;

            if (species.staleness < STALE_SPECIES
                    || species.topFitness >= maxFitness)
                survived.add(species);
        }

        species.clear();
        species.addAll(survived);
    }

    private void removeWeakSpecies() {
        final List<Species> survived = new ArrayList<Species>();

        final double sum = totalAverageFitness();
        for (final Species species : this.species) {
            final double breed = Math.floor(species.averageFitness / sum * POPULATION);
            if (breed >= 1.0)
                survived.add(species);
        }

        species.clear();
        species.addAll(survived);
    }

    private double totalAverageFitness() {
        double total = 0;
        for (final Species species : this.species)
            total += species.averageFitness;
        return total;
    }

    public static boolean isInput(final int id){ return id < INPUTS; }
    public static boolean isOutput(final int id){ return id >= INPUTS && id < INPUTS + OUTPUTS; }

}
