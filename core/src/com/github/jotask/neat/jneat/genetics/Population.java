package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.util.JRandom;

import java.util.*;

import static com.github.jotask.neat.jneat.util.Ref.STALE_SPECIES;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Population implements Json.Serializable{

    public static int innovation = Ref.INPUTS + Ref.OUTPUTS - 1;

    public final LinkedList<Specie> species;
    public int generation;
    public double maxFitness;

    public Population() {
        this.species = new LinkedList<Specie>();
        this.generation = 0;
        this.maxFitness = 0.0;
    }

    private void addToSpecies(final Genome child) {
        for (final Specie species : this.species)
            if (child.sameSpecies(species.getGenomes().get(0))) {
                species.getGenomes().add(child);
                return;
            }

        final Specie childSpecies = new Specie();
        childSpecies.getGenomes().add(child);
        species.add(childSpecies);
    }

    private void cullSpecies(final boolean cutToOne) {
        for (final Specie species : this.species) {
            Collections.sort(species.getGenomes(), new Comparator<Genome>() {

                @Override
                public int compare(final Genome o1, final Genome o2) {
                    final double cmp = o2.fitness - o1.fitness;
                    return cmp == 0.0 ? 0 : cmp > 0.0 ? 1 : -1;
                }
            });

            double remaining = Math.ceil(species.getGenomes().size() / 2.0);
            if (cutToOne)
                remaining = 1.0;

            while (species.getGenomes().size() > remaining)
                species.getGenomes().remove(species.getGenomes().size() - 1);
        }
    }

    public void initialize() {
        for (int i = 0; i < Ref.POPULATION; ++i) {
            final Genome basic = new Genome();
            basic.mutate();
            addToSpecies(basic);
        }
    }

    public void newGeneration() {

        // kill overpopulation
        cullSpecies(false);

        rankGlobally();

        // kill contaminated species
        removeStaleSpecies();
        rankGlobally();
        for (final Specie species : this.species)
            species.calculateAverageFitness();
        removeWeakSpecies();
        final double sum = totalAverageFitness();
        final List<Genome> children = new ArrayList<Genome>();
        for (final Specie species : this.species) {
            final double breed = Math.floor(species.averageFitness / sum * Ref.POPULATION) - 1.0;
            for (int i = 0; i < breed; ++i)
                children.add(species.breedChild());
        }
        cullSpecies(true);
        while (children.size() + species.size() < Ref.POPULATION) {
            final Specie species = this.species.get(JRandom.randomIndex(this.species));
            children.add(species.breedChild());
        }
        for (final Genome child : children)
            addToSpecies(child);

        while(this.species.size() < Ref.POPULATION * .5f)
            diversity();

        generation++;
    }

    private void diversity(){

        final LinkedList<Specie> other = new LinkedList<Specie>(this.species);
        Collections.sort(other, new Comparator<Specie>() {
            @Override
            public int compare(Specie o1, Specie o2) {
                int tmp = o1.getGenomes().size() - o2.getGenomes().size();
                return tmp == 0 ? 0: tmp > 0 ? -1 : 1;
            }
        });

        final Specie mother = other.getFirst();

        final Specie a = new Specie();
        final Specie b = new Specie();

        boolean tmp = false;
        for(final Genome g: mother.getGenomes()){
            if(tmp){
                a.getGenomes().add(g);
            }else{
                b.getGenomes().add(g);
            }
            tmp = !tmp;
        }

        this.species.remove(mother);

        this.species.add(a);
        this.species.add(b);

    }

    private void rankGlobally() {
        final List<Genome> global = new ArrayList<Genome>();
        for (final Specie species : this.species)
            for (final Genome genome : species.getGenomes())
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
        final List<Specie> survived = new ArrayList<Specie>();
        for (final Specie species : this.species) {
            Collections.sort(species.getGenomes(), new Comparator<Genome>() {

                @Override
                public int compare(final Genome o1, final Genome o2) {
                    final double cmp = o2.fitness - o1.fitness;
                    return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
                }
            });

            if (species.getGenomes().get(0).fitness > species.topFitness) {
                species.topFitness = species.getGenomes().get(0).fitness;
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
        final List<Specie> survived = new ArrayList<Specie>();

        final double sum = totalAverageFitness();
        for (final Specie species : this.species) {
            final double breed = Math
                    .floor(species.averageFitness / sum * Ref.POPULATION);
            if (breed >= 1.0)
                survived.add(species);
        }

        species.clear();
        species.addAll(survived);
    }

    private double totalAverageFitness() {
        double total = 0;
        for (final Specie species : this.species)
            total += species.averageFitness;
        return total;
    }

    public List<Specie> getSpecies() { return species; }

    @Override
    public void write(Json json) {
        json.writeValue("generation", this.generation);
        json.writeValue("maxFitness", this.maxFitness);
        json.writeValue("innovation", Population.innovation);
        json.writeValue("Species", this.species);
    }

    @Override
    public void read(Json json, JsonValue data) {
        this.generation = data.getInt("generation");
        this.maxFitness = data.getDouble("maxFitness");
        this.innovation = data.getInt("innovation");
        for (JsonValue v : data.get("Species")) {
            Specie s = json.readValue(Specie.class, v);
            species.add(s);
        }
    }
}
