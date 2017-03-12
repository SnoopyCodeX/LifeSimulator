package com.github.jotask.neat.jneat.genetics;

import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.util.JRandom;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Population {

    public static int innovation;

    private final LinkedList<Specie> species;
    public int generation;
    public double maxFitness;

    public Population() {
        this.species = new LinkedList<Specie>();
        this.generation = 0;
        this.maxFitness = 0.0;
    }

    public void initialize(){
        for(int i = 0; i < Ref.POPULATION; i++){
            final Genome basic = new Genome();
            basic.maxNeuron = (Ref.INPUTS + Ref.OUTPUTS) - 1;
            basic.mutate();
            addSpecies(basic);
        }
    }

    private void addSpecies(final Genome child){
        final Specie childSpecie = new Specie(child);
        species.add(childSpecie);
    }

    private void cullSpecies(final boolean cutToOne){

        final LinkedList<Specie> survived = new LinkedList<Specie>(this.species);

        Collections.sort(survived, new Comparator<Specie>() {
            @Override
            public int compare(final Specie o1, final Specie o2) {
                final double cmp = o2.genome.fitness - o1.genome.fitness;
                return cmp == 0.0 ? 0 : cmp > 0.0 ? 1 : -1;
            }
        });

        double remaining = Math.ceil(survived.size() / 2.0);

        if (cutToOne) {
            remaining = 1.0;
        }

        while (survived.size() > remaining) {
            survived.removeLast();
        }

        this.species.clear();
        this.species.addAll(survived);


    }

    private void rankGlobally(){
        final LinkedList<Genome> global = new LinkedList<Genome>();
        for (final Specie specie : this.species) {
            global.add(specie.genome);
        }

        Collections.sort(global, new Comparator<Genome>() {

            @Override
            public int compare(final Genome o1, final Genome o2) {
                final double cmp = o1.fitness - o2.fitness;
                return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
            }
        });

        for (int i = 0; i < global.size(); i++) {
            global.get(i).globalRank = i;
        }

    }

    public void removeStaleSpecies(){
        final LinkedList<Specie> survived = new LinkedList<Specie>();

        Collections.sort(this.species, new Comparator<Specie>() {

            @Override
            public int compare(final Specie o1, final Specie o2) {
                final double cmp = o2.genome.fitness - o1.genome.fitness;
                return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
            }
        });

        for(final Specie specie: this.species){
            if(specie.genome.fitness > specie.topFitness){
                specie.topFitness = specie.genome.fitness;
                specie.stanles = 0;
            }else{
                specie.stanles++;
            }

            if(specie.stanles < Ref.STALE_SPECIES || specie.topFitness >= maxFitness){
                survived.add(specie);
            }

        }
        this.species.clear();
        this.species.addAll(survived);
    }

    private void removeWeakSpecies(){
        final LinkedList<Specie> survived = new LinkedList<Specie>();
        final double sum = totalAverageFitness();
        for(final Specie specie: this.species){
            final double breed = Math.floor(specie.averageFitness / sum * Ref.POPULATION);
            if(breed >= 1.0){
                survived.add(specie);
            }
        }
        this.species.clear();
        this.species.addAll(survived);
    }

    private double totalAverageFitness() {
        double total = 0;
        for (final Specie species : this.species) {
            total += species.averageFitness;
        }
        return total;
    }

    public void newGeneration(){
        this.cullSpecies(false);
        this.rankGlobally();
        this.removeStaleSpecies();
        this.rankGlobally();
        for(final Specie specie: this.species){
            specie.calculateAverageFitness();
        }
        this.removeWeakSpecies();
        final double sum = totalAverageFitness();
        final LinkedList<Genome> childrens = new LinkedList<Genome>();
        for(final Specie specie: this.species){
            final double breed = Math.floor(specie.averageFitness / sum * Ref.POPULATION) - 1.0;
            for(int i = 0; i < breed; i++){
                final Genome genome = this.breedChildren(specie);
                childrens.add(genome);
            }
        }
        this.cullSpecies(true);
        while(childrens.size() + this.species.size() > Ref.POPULATION){
            final Specie specie = this.species.get(JRandom.randomIndex(this.species));
            final Genome genome = this.breedChildren(specie);
            childrens.add(genome);
        }

        for(final Genome child: childrens){
            this.addSpecies(child);
        }
        this.generation++;
    }

    private Genome breedChildren(final Specie mother){
        // TODO pick father with probability
        Specie father = mother;
        while (mother == father){
            father = this.species.get(JRandom.randomIndex(this.species));
        }
        return this.breedChild(mother.genome, father.genome);
    }

    private Genome breedChild(final Genome mother, final Genome father){
        final Genome child;
        if(JRandom.random.nextDouble() < Ref.CROSSOVER){
            child = this.crossover(mother, father);
        }else{
            child = mother;
        }
        child.mutate();
        return child;
    }

    private Genome crossover(Genome mother, Genome father){
        if(father.fitness > mother.fitness){
            final Genome tmp = mother;
            mother = father;
            father = tmp;
        }

        final Genome child = new Genome();

        outerLoop: for(final Synapse gene1: mother.getGenes()){
            for(final Synapse gene2: mother.getGenes()){
                if(gene1.getInnovation() == gene2.getInnovation()){
                    if(JRandom.random.nextBoolean() && gene2.isEnabled()){
                        child.getGenes().add(new Synapse(gene2));
                        continue outerLoop;
                    }else {
                        break;
                    }
                }
            }
            child.getGenes().add(new Synapse(gene1));
        }

        child.maxNeuron = Math.max(mother.maxNeuron, father.maxNeuron);

        for(int i = 0; i < 7; i++){
            child.mutationRates[i] = mother.mutationRates[i];
        }

        return child;
    }

    public LinkedList<Specie> getSpecies() { return species; }
}
