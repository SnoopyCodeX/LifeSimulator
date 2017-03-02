package com.github.jotask.neat.jneat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.jotask.neat.engine.JRandom.random;
import static com.github.jotask.neat.jneat.Constants.*;

/**
 * NeatAlgorithm
 *
 * @author Jose Vives Iznardo
 * @since 15/02/2017
 */
public class Population implements Json.Serializable{

    public final List<Species> species = new ArrayList<Species>();
    public int generation = 0;
    public static int innovation = OUTPUTS;
    public double maxFitness = 0.0;

    public Population(){ }

    public void init(){
        for (int i = 0; i < POPULATION; ++i) {
            final Genome basic = new Genome();
            addToSpecies(basic);
//            basic.maxNeuron = INPUTS;
            basic.mutate();
        }
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

            Collections.sort(species.genomes);

            double remaining = Math.ceil(species.genomes.size() / 2.0);
            if (cutToOne)
                remaining = 1.0;

            while (species.genomes.size() > remaining)
                species.genomes.remove(species.genomes.size() - 1);
        }
    }

    void newGeneration() {

        // FIXME loosing too many species over the time

        cullSpecies(false);
        rankGlobally();
        removeStaleSpecies();
        rankGlobally();

        for (final Species species : this.species){
            species.calculateAverageFitness();
        }

        // TODO loosing species here
        removeWeakSpecies();

        final double sum = totalAverageFitness();

        final List<Genome> children = new ArrayList<Genome>();
        for (final Species species : this.species) {
            final double breed = Math.floor(species.averageFitness / sum * POPULATION) - 1.0;
            for (int i = 0; i < breed; ++i) {
                Genome breedChild = species.breedChild();
                children.add(breedChild);
            }
        }

        cullSpecies(true);

        // FIXME some time species is zero
//        int m = POPULATION - (children.size() + species.size());
//        int s = species.size();

//        System.out.println("adding " + m + " species. ");
//        System.out.println("Species: " + species.size() + " Children's: " + children.size());

        if(this.species.isEmpty()){
            throw new RuntimeException("Species is empty");
        }

        while (children.size() + species.size() < POPULATION) {
            final Species species = this.species.get(random.nextInt(this.species.size()));
            children.add(species.breedChild());
        }

        for (final Genome child : children) {
            addToSpecies(child);
        }

//        System.out.println("pre: " + s + " post: " + species.size());
//        System.out.println("------------------------------");

        generation++;

    }

    private void rankGlobally() {
        final List<Genome> global = new ArrayList<Genome>();
        for (final Species species : this.species)
            for (final Genome genome : species.genomes)
                global.add(genome);

        // // TODO This comparator was inverse
        Collections.sort(global);
//        Collections.reverse(global);

        for (int i = 0; i < global.size(); ++i) {
            global.get(i).globalRank = i;
        }

    }

    private void removeStaleSpecies() {
        final List<Species> survived = new ArrayList<Species>();
        for (final Species species : this.species) {
            Collections.sort(species.genomes);

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

    @Override
    public void write(Json json) {
        json.writeObjectStart("population");
        json.writeValue("generation", this.generation, Integer.class);
        json.writeValue("fitness", this.maxFitness, Double.class);
        json.writeArrayStart("species");
        for(Species s: this.species){
            json.writeValue(s);
        }
        json.writeArrayEnd();
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

}
