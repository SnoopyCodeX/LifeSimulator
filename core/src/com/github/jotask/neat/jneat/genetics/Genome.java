package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.jneat.network.Network;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.github.jotask.neat.jneat.util.Ref.*;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Genome implements Json.Serializable{

    private final LinkedList<Synapse> genes;
    public double fitness;

    int maxNeuron;
    int globalRank;
    double step_size = STEP_SIZE;

    private Network network;

    Genome() {
        this.genes = new LinkedList<Synapse>();
        this.fitness = 0.0;
        this.maxNeuron =  Ref.INPUTS + Ref.OUTPUTS - 1;
        this.globalRank = 0;
    }

    Genome(final Genome genome) {
        this();
        for(final Synapse gene: genome.getGenes()){
            this.genes.add(new Synapse(gene));
        }
        this.maxNeuron = genome.maxNeuron;
        this.step_size = genome.step_size;
    }

    private boolean containsLink(final Synapse link) {
        for (final Synapse gene : genes) {
            if (gene.getInput() == link.getInput() && gene.getOutput() == link.getOutput()) {
                return true;
            }
        }
        return false;
    }

    private double disjoint(final Genome genome) {
        double disjointGenes = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes) {
                if (gene.getInnovation() == otherGene.getInnovation()) {
                    continue search;
                }
            }
            ++disjointGenes;
        }
        return disjointGenes / Math.max(genes.size(), genome.genes.size());
    }

    public void generateNetwork(){
        this.network = new Network(this.genes);
    }

    void mutate() {

        if (JRandom.random() < CONN_MUTATION)
            mutatePoint();

        if (JRandom.random() < LINK_MUTATION)
            mutateLink(false);

        if (JRandom.random() < BIAS_MUTATION)
            mutateLink(true);

        if (JRandom.random() < NODE_MUTATION)
            mutateNode();

        if (JRandom.random() < ENABLE_MUTATION)
            mutateEnableDisable(true);

        if (JRandom.random() < DISABLE_MUTATION)
            mutateEnableDisable(false);

    }

    private void mutateEnableDisable(final boolean enable) {
        final List<Synapse> candidates = new ArrayList<Synapse>();
        for (final Synapse gene : genes) {
            if (gene.isEnabled() != enable) {
                candidates.add(gene);
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        final Synapse gene = candidates.get(JRandom.randomIndex(candidates));
        gene.setEnabled(!gene.isEnabled());
    }

    private void mutateLink(final boolean forceBias) {
        final int neuron1 = randomNeuron(false, true);
        final int neuron2 = randomNeuron(true, false);

        final Synapse newLink = new Synapse();
        newLink.setInput(neuron1);
        newLink.setOutput(neuron2);

        if (forceBias) {
            newLink.setInput(Inputs.bias.ordinal());
        }

        if (containsLink(newLink)) {
            return;
        }

        newLink.setInnovation(++Population.innovation);
        newLink.setWeight(JRandom.random() * 4.0 - 2.0);

        genes.add(newLink);
    }

    private void mutateNode() {
        if (genes.isEmpty()) {
            return;
        }

        final Synapse gene = genes.get(JRandom.randomIndex(genes));

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

    private void mutatePoint() {

        this.step_size *= JRandom.nextBoolean() ? 0.95 : 1.05263;

        for (final Synapse gene : genes) {
            if (JRandom.random() < PERTURBATION) {
                double w = gene.getWeight();
                w += JRandom.random() * this.step_size * 2.0 - this.step_size;
                gene.setWeight(w);
            } else {
                gene.setWeight(JRandom.random() * 4.0 - 2.0);
            }
        }
    }

    private int randomNeuron(final boolean nonInput, final boolean nonOutput) {
        final List<Integer> neurons = new ArrayList<Integer>();

        if (!nonInput) {
            for (int i = 0; i < INPUTS - 1; i++) {
                neurons.add(i);
            }
        }

        if (!nonOutput) {
            for (int i = 0; i < OUTPUTS; i++) {
                neurons.add(INPUTS + i);
            }
        }

        for (final Synapse gene : genes) {
            if ((!nonInput || gene.getInput() >= INPUTS) && (!nonOutput || gene.getInput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getInput());
            }
            if ((!nonInput || gene.getOutput() >= INPUTS) && (!nonOutput || gene.getOutput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getOutput());
            }
        }

        return neurons.get(JRandom.randomIndex(neurons));
    }

    boolean sameSpecies(final Genome genome) {
        final double dd = DELTA_DISJOINT * disjoint(genome);
        final double dw = DELTA_WEIGHTS * weights(genome);
        return dd + dw < DELTA_THRESHOLD;
    }

    private double weights(final Genome genome) {
        double sum = 0.0;
        double coincident = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes) {
                if (gene.getInnovation() == otherGene.getInnovation()) {
                    sum += Math.abs(gene.getWeight() - otherGene.getWeight());
                    ++coincident;
                    continue search;
                }
            }
        }
        return sum / coincident;
    }

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

    public List<Synapse> getGenes() { return genes; }

    public Network getNetwork() { return network; }

}
