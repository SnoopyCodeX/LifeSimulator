package com.github.jotask.neat.jneat;

import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.github.jotask.neat.jneat.Constants.*;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Population {

    public final List<Species> species;
    public int generation;
    public static int innovation = OUTPUTS;
    public double maxFitness;

    public Population(){
        this.species = new ArrayList<Species>();
        this.generation = 0;
        this.maxFitness = 0.0;
    }

    // Create basic genomes
    public void init(){
        for(int i = 0; i < POPULATION; i++){
            final Genome basic = new Genome();
            basic.mutate();
            addToSpecies(basic);
        }
    }

    private void addToSpecies(final Genome child) {

        for (final Species species : this.species) {
            if (child.sameSpecies(species.genomes.get(0))) {
                species.genomes.add(child);
                return;
            }
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
                    final double cmp = o2.getFitness() - o1.getFitness();
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

    private void rankGlobally() {

        final List<Genome> global = new ArrayList<Genome>();

        for (final Species species : this.species) {
            for (final Genome genome : species.genomes) {
                global.add(genome);
            }
        }

        Collections.sort(global, new Comparator<Genome>() {

            @Override
            public int compare(final Genome o1, final Genome o2) {
                final double cmp = o1.getFitness() - o2.getFitness();
                return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
            }
        });

        for (int i = 0; i < global.size(); ++i) {
            global.get(i).setGlobalRank(i);
        }

    }

    private void removeStaleSpecies() {
        final List<Species> survived = new ArrayList<Species>();

        for (final Species species : this.species) {

            Collections.sort(species.genomes, new Comparator<Genome>() {
                @Override
                public int compare(final Genome o1, final Genome o2) {
                    final double cmp = o2.getFitness() - o1.getFitness();
                    return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
                }
            });

            if (species.genomes.get(0).getFitness() > species.topFitness) {
                species.topFitness = species.genomes.get(0).getFitness();
                species.staleness = 0;
            } else {
                species.staleness++;
            }

            if (species.staleness < STALE_SPECIES || species.topFitness >= maxFitness) {
                survived.add(species);
            }

        }

        species.clear();
        species.addAll(survived);

    }

    private void removeWeakSpecies() {
        final List<Species> survived = new ArrayList<Species>();

        final double sum = totalAverageFitness();
        for (final Species species : this.species) {

            final double breed = Math.floor(species.averageFitness / sum * POPULATION);

            if (breed >= 1.0) {
                survived.add(species);
            }

        }

        species.clear();
        species.addAll(survived);

    }

    private double totalAverageFitness() {
        double total = 0;
        for (final Species species : this.species) {
            total += species.averageFitness;
        }
        return total;
    }

    public void newGeneration() {

        cullSpecies(false);
        rankGlobally();
        removeStaleSpecies();
        rankGlobally();

        for (final Species species : this.species) {
            species.calculateAverageFitness();
        }

        removeWeakSpecies();

        final double sum = totalAverageFitness();
        final List<Genome> children = new ArrayList<Genome>();
        for (final Species species : this.species) {
            final double breed = Math.floor(species.averageFitness / sum * POPULATION) - 1.0;
            for (int i = 0; i < breed; i++) {
                children.add(species.breedChild());
            }
        }

        cullSpecies(true);
        while (children.size() + species.size() < POPULATION) {
            final Species species = this.species.get(JRandom.randomIndex(this.species));
            children.add(species.breedChild());
        }

        for (final Genome child : children) {
            addToSpecies(child);
        }

        generation++;

    }

    public boolean isMaxFitness(final double f){
        if(this.maxFitness < f){
            this.maxFitness = f;
            return true;
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Population clone");
    }
}
