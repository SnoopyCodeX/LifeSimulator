package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.jneat.network.Network;
import com.github.jotask.neat.jneat.util.JException;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.jneat.util.Util;
import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.List;

import static com.github.jotask.neat.jneat.util.Ref.*;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Genome implements Json.Serializable{

    private final ArrayList<Synapse> genes;

    public double fitness;
    public int maxNeuron;
    public int globalRank;

    public double step_size;

    private Network network;

    public Genome() {
        this.genes = new ArrayList<Synapse>();
        this.fitness = 0.0;
        this.maxNeuron = (Ref.INPUTS + Ref.OUTPUTS) - 1;
        this.globalRank = 0;
        this.step_size = Ref.STEP_SIZE;
    }

    public boolean containsLink(final Synapse synapse){
        for(final Synapse s: genes){
            if((s.getInput() == synapse.getInput())&&(s.getOutput() == synapse.getOutput())){
                return true;
            }
        }
        return false;
    }

    public double disjoint(final Genome genome){
        double disjointGenes = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes)
                if (gene.getInnovation() == otherGene.getInnovation())
                    continue search;
            disjointGenes++;
        }
        return disjointGenes / Math.max(genes.size(), genome.genes.size());
    }

    public void generateNetwork(){ this.network = new Network(this.genes); }

    public void mutate() {

        // FIXME sometimes it connects to two inputss

        if (JRandom.random() < CONN_MUTATION) {
            mutatePoint();
        }

        if(JRandom.random() < LINK_MUTATION){
            mutateLink(false);
        }

        if(JRandom.random() < BIAS_MUTATION){
            mutateLink(true);
        }

        if(JRandom.random() < NODE_MUTATION){
            mutateNode();
        }

//        if (JRandom.random() < ENABLE_MUTATION) {
//            mutateEnableDisable(true);
//        }

//        if(JRandom.random() < DISABLE_MUTATION){
//            mutateEnableDisable(false);
//        }

    }

    public void mutateEnableDisable(final boolean enable) {
        final List<Synapse> candidates = new ArrayList<Synapse>();
        for (final Synapse gene : genes) {
            if (gene.isEnabled() != enable) {
                candidates.add(gene);
            }
        }

        if (candidates.isEmpty())
            return;

        final Synapse gene = candidates.get(JRandom.randomIndex(candidates));
        gene.setEnabled(!gene.isEnabled());

    }

    public void mutateLink(final boolean forceBias) {
        final int input;
        final int output;

        while(true){
            int i = randomNeuron(true, false);
            int j = randomNeuron(false, true);
            if(Util.isInput(i) && Util.isInput(j)){
                continue;
            }
            if(Util.isOutput(i) && Util.isOutput(j)){
                continue;
            }
            if(i != j){
                input = i;
                output = j;
                break;
            }
        }

        final Synapse newLink = new Synapse();
        newLink.setInput(input);
        newLink.setOutput(output);

        if (forceBias) {
            newLink.setInput(Ref.Inputs.bias.ordinal());
        }

        if (containsLink(newLink))
            return;

        newLink.setInnovation(++Population.innovation);
        newLink.setWeight(JRandom.random() * 4.0 - 2.0);

        this.addLink(newLink);

    }

    public void mutateNode() {

        if (genes.isEmpty())
            return;

//        if(this.genes.size() > Ref.MAX_NEURONS){
//            return;
//        }

        final Synapse gene = genes.get(JRandom.randomIndex(genes));

        if (!gene.isEnabled())
            return;

//        System.out.println(gene.toString() + " : " + maxNeuron);

        gene.setEnabled(false);

        ++maxNeuron;

        final Synapse gene1 = new Synapse(gene);
        gene1.setOutput(maxNeuron);
        gene1.setWeight(1.0);
        gene1.setInnovation(++Population.innovation);
        gene1.setEnabled(true);
        this.addLink(gene1);

        final Synapse gene2 = new Synapse(gene);
        gene2.setInput(maxNeuron);
        gene2.setInnovation(++Population.innovation);
        gene2.setEnabled(true);
        this.addLink(gene2);

    }

    public void mutatePoint() {

        this.step_size *= JRandom.nextBoolean() ? 0.95: 1.05263;

        for (final Synapse gene : genes){
            if (JRandom.random() < PERTURBATION) {
                double w = gene.getWeight();
                w += JRandom.random() * step_size * 2.0 - step_size;
                gene.setWeight(w);
            }else{
                gene.setWeight(JRandom.random() * 4.0 - 2.0);
            }
        }
    }

    public boolean addLink(final Synapse link){
        if(containsLink(link))
            return false;

        if(link.getInput() == link.getOutput()){
            throw new JException("same link");
        }

        return this.genes.add(link);
    }

    private int randomNeuron(final boolean input, final boolean output) {

        final List<Integer> neurons = new ArrayList<Integer>();

        if (input) {
            for (int i = 0; i < INPUTS - 1; i++) {
                neurons.add(i);
            }
        }

        if (output){
            for (int i = 0; i < OUTPUTS; i++){
                neurons.add(INPUTS + i);}
        }

        for (final Synapse gene : genes) {
            if ((input || gene.getInput() >= INPUTS) && (output || gene.getInput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getInput());
            }
            if ((input || gene.getOutput() >= INPUTS) && (output || gene.getOutput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getOutput());
            }
        }

        return neurons.get(JRandom.randomIndex(neurons));

    }

    public boolean sameSpecies(Genome genome) {
        final double dd = DELTA_DISJOINT * disjoint(genome);
        final double dw = DELTA_WEIGHTS * weights(genome);
        return dd + dw < DELTA_THRESHOLD;
    }

    public double weights(final Genome genome) {
        double sum = 0.0;
        double coincident = 0.0;
        search: for (final Synapse gene : genes)
            for (final Synapse otherGene : genome.genes)
                if (gene.getInnovation() == otherGene.getInnovation()) {
                    sum += Math.abs(gene.getWeight() - otherGene.getWeight());
                    ++coincident;
                    continue search;
                }
        return sum / coincident;
    }

    public List<Synapse> getGenes() { return genes; }

    public Network getNetwork() { return network; }

    @Override
    public void write(Json json) {
        json.writeValue("fitness", this.fitness);
        json.writeValue("maxNeuron", this.maxNeuron);
        json.writeValue("globalRank", this.globalRank);
        json.writeValue("genes", this.genes);
    }

    @Override
    public void read(Json json, JsonValue data) {
        this.fitness = data.getDouble("fitness");
        this.globalRank = data.getInt("globalRank");
        this.maxNeuron = data.getInt("maxNeuron");
        for (JsonValue v : data.get("genes")) {
            Synapse syn = json.readValue(Synapse.class, v);
            this.genes.add(syn);
        }
    }

    @Override
    public String toString() {
        return genes.toString();
    }
}
