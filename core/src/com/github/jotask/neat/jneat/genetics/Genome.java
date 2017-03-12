package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.jneat.network.Network;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.util.JRandom;

import java.util.*;

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
    public final double[] mutationRates = new double[] { CONN_MUTATION,
            LINK_MUTATION, BIAS_MUTATION, NODE_MUTATION, ENABLE_MUTATION,
            DISABLE_MUTATION, STEP_SIZE };

    private Network network;

    public Genome() {
        this.genes = new ArrayList<Synapse>();
        this.fitness = 0.0;
        this.maxNeuron = 0;
        this.globalRank = 0;
    }

    public Genome(final Genome genome){
        this();
        this.fitness = genome.fitness;
        this.maxNeuron = genome.maxNeuron;
        this.globalRank = genome.globalRank;
        for(final Synapse synapse: genome.genes){
            this.genes.add(new Synapse(synapse));
        }
        for(int i = 0; i < 7; i++){
            this.mutationRates[i] = genome.mutationRates[i];
        }
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

        // FIXME sometimes it connects to two inputs

        for (int i = 0; i < 7; i++)
            mutationRates[i] *= JRandom.random.nextBoolean() ? 0.95 : 1.05263;

        if (JRandom.random.nextDouble() < mutationRates[0])
            mutatePoint();

        double prob = mutationRates[1];
        while (prob > 0) {
            if (JRandom.random.nextDouble() < prob)
                mutateLink(false);
            --prob;
        }

        prob = mutationRates[2];
        while (prob > 0) {
            if (JRandom.random.nextDouble() < prob)
                mutateLink(true);
            --prob;
        }

        prob = mutationRates[3];
        while (prob > 0) {
            if (JRandom.random.nextDouble() < prob)
                mutateNode();
            --prob;
        }

        prob = mutationRates[4];
        while (prob > 0) {
            if (JRandom.random.nextDouble() < prob)
                mutateEnableDisable(true);
            --prob;
        }

        prob = mutationRates[5];
        while (prob > 0) {
            if (JRandom.random.nextDouble() < prob)
                mutateEnableDisable(false);
            --prob;
        }
    }

    public void mutateEnableDisable(final boolean enable) {
        final List<Synapse> candidates = new ArrayList<Synapse>();
        for (final Synapse gene : genes)
            if (gene.isEnabled() != enable)
                candidates.add(gene);

        if (candidates.isEmpty())
            return;

        final Synapse gene = candidates.get(JRandom.random.nextInt(candidates.size()));
        gene.setEnabled(!gene.isEnabled());
    }

    public void mutateLink(final boolean forceBias) {
        final int neuron1 = randomNeuron(false, true);
        final int neuron2 = randomNeuron(true, false);

        final Synapse newLink = new Synapse();
        newLink.setInput(neuron1);
        newLink.setOutput(neuron2);

        if (forceBias) {
            newLink.setInput(Ref.Inputs.bias.ordinal());
        }

        if (containsLink(newLink))
            return;

        newLink.setInnovation(++Population.innovation);
        newLink.setWeight(JRandom.random.nextDouble() * 4.0 - 2.0);

        genes.add(newLink);
    }

    public void mutateNode() {
        if (genes.isEmpty())
            return;

        final Synapse gene = genes.get(JRandom.random.nextInt(genes.size()));
        if (!gene.isEnabled())
            return;
        gene.setEnabled(false);

        ++maxNeuron;

        final Synapse gene1 = new Synapse(gene);
        gene1.setOutput(maxNeuron);
        gene1.setWeight(1.0);
        gene1.setInnovation(++Population.innovation);
        gene1.setEnabled(true);
        genes.add(gene1);

        final Synapse gene2 = new Synapse(gene);
        gene2.setInput(maxNeuron);
        gene2.setInnovation(++Population.innovation);
        gene2.setEnabled(true);
        genes.add(gene2);
    }

    public void mutatePoint() {
        for (final Synapse gene : genes){
            if (JRandom.random.nextDouble() < PERTURBATION) {
                double w = gene.getWeight();
                w += JRandom.random.nextDouble() * mutationRates[6] * 2.0 - mutationRates[6];
                gene.setWeight(w);
            }else{
                gene.setWeight(JRandom.random.nextDouble() * 4.0 - 2.0);
            }
        }
    }

    public int randomNeuron(final boolean nonInput, final boolean nonOutput) {
        final List<Integer> neurons = new ArrayList<Integer>();

        if (!nonInput)
            for (int i = 0; i < INPUTS; ++i)
                neurons.add(i);

        if (!nonOutput)
            for (int i = 0; i < OUTPUTS; ++i)
                neurons.add(INPUTS + i);

        for (final Synapse gene : genes) {
            if ((!nonInput || gene.getInput() >= INPUTS) && (!nonOutput || gene.getInput() >= INPUTS + OUTPUTS))
                neurons.add(gene.getInput());
            if ((!nonInput || gene.getOutput() >= INPUTS) && (!nonOutput || gene.getOutput() >= INPUTS + OUTPUTS))
                neurons.add(gene.getOutput());
        }

        return neurons.get(JRandom.random.nextInt(neurons.size()));
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

}
