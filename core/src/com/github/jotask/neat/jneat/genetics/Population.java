package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.config.Config;
import com.github.jotask.neat.jneat.Jota;
import com.github.jotask.neat.jneat.util.Constants;
import com.github.jotask.neat.util.JRandom;

import java.util.*;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Population implements Json.Serializable{

    private final int POPULATION;
    private final int STALE_SPECIES;

    private static int innovation = Constants.OUTPUTS;

    public static int newInnovation(){
        innovation++;
        return innovation;
    }

    private final List<Specie> species;
    private int generation;
    public double maxFitness;

    public Population() {
        final Config cfg = Jota.get().getConfig();
        POPULATION = new Integer(cfg.get(Config.Property.POPULATION));
        STALE_SPECIES = new Integer(cfg.get(Config.Property.STALE_SPECIES));
        this.species = new ArrayList<Specie>();
        this.generation = 0;
        this.maxFitness = 0.0;
    }

    private void addToSpecies(final Genome child) {
        for(final Specie s: this.species){
            if(child.sameSpecies(s.getGenomes().getFirst())){
                s.getGenomes().push(child);
                return;
            }
        }
        final Specie s = new Specie();
        s.getGenomes().push(child);
        this.species.add(s);
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

            final double remaining;
            if(cutToOne){
                remaining = 1.0;
            }else{
                remaining = Math.ceil(species.getGenomes().size() / 2.0);
            }

            while (species.getGenomes().size() > remaining) {
                species.getGenomes().remove(species.getGenomes().size() - 1);
            }
        }
    }

    public void initialize() {
        for (int i = 0; i < POPULATION; ++i) {
            final Genome basic = new Genome();
            basic.mutate();
            addToSpecies(basic);
        }
    }

    public void newGeneration() {
        // Cull the bottom half od each species
        cullSpecies(false);
        rankGlobally();
        removeStaleSpecies();
        rankGlobally();
        for (final Specie species : this.species) {
            species.calculateAverageFitness();
        }
        removeWeakSpecies();
        final double sum = totalAverageFitness();
        final List<Genome> children = new ArrayList<Genome>();
        for(final Specie s: this.species){
            final double breed = Math.floor(s.averageFitness / sum * POPULATION) - 1.0;
            for(int i = 0; i < breed; i++){
                children.add(s.breedChild());
            }
        }

        // Cull all but the top member of each species
        cullSpecies(true);
        while(children.size() + this.species.size() < POPULATION){
            final Specie s = this.species.get(JRandom.randomIndex(this.species));
            children.add(s.breedChild());
        }

        for(final Genome g: children){
            this.addToSpecies(g);;
        }

        while(this.species.size() < POPULATION * .5f) {
            diversity();
        }

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
        for (final Specie species : this.species) {
            for (final Genome genome : species.getGenomes()) {
                global.add(genome);
            }
        }

        Collections.sort(global, new Comparator<Genome>() {

            @Override
            public int compare(final Genome o1, final Genome o2) {
                final double cmp = o1.fitness - o2.fitness;
                return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
            }
        });

        for (int i = 0; i < global.size(); ++i) {
            global.get(i).globalRank = i;
        }
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

            if (species.getGenomes().getFirst().fitness > species.topFitness) {
                species.topFitness = species.getGenomes().getFirst().fitness;
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
        final List<Specie> survived = new ArrayList<Specie>();

        final double sum = totalAverageFitness();
        for (final Specie species : this.species) {
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
        for (final Specie species : this.species) {
            total += species.averageFitness;
        }
        return total;
    }

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

    public List<Specie> getSpecies() { return species; }

    public int getGeneration() { return generation; }

}
