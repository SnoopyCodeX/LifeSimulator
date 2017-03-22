package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.config.Config;
import com.github.jotask.neat.jneat.Jota;
import com.github.jotask.neat.jneat.util.Constants;
import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.github.jotask.neat.jneat.util.Constants.*;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Genome implements Json.Serializable{

    private final float CONN_MUTATION;
    private final float LINK_MUTATION;
    private final float BIAS_MUTATION;
    private final float ENABLE_MUTATION;
    private final float DISABLE_MUTATION;
    private final float NODE_MUTATION;
    private final float PERTURBATION;
    private final float DELTA_DISJOINT;
    private final float DELTA_THRESHOLD;
    private final float DELTA_WEIGHTS;

    private final LinkedList<Synapse> genes;
    public double fitness;

    int maxNeuron;
    int globalRank;
    double step_size;

    Genome() {

        final Config cfg = Jota.get().getConfig();
        CONN_MUTATION = new Float(cfg.get(Config.Property.CONN_MUTATION));
        LINK_MUTATION = new Float(cfg.get(Config.Property.LINK_MUTATION));
        BIAS_MUTATION = new Float(cfg.get(Config.Property.BIAS_MUTATION));
        NODE_MUTATION = new Float(cfg.get(Config.Property.NODE_MUTATION));
        ENABLE_MUTATION = new Float(cfg.get(Config.Property.ENABLE_MUTATION));
        DISABLE_MUTATION = new Float(cfg.get(Config.Property.DISABLE_MUTATION));
        PERTURBATION = new Float(cfg.get(Config.Property.PERTURBATION));
        DELTA_DISJOINT = new Float(cfg.get(Config.Property.DELTA_DISJOINT));
        DELTA_THRESHOLD = new Float(cfg.get(Config.Property.DELTA_THRESHOLD));
        DELTA_WEIGHTS = new Float(cfg.get(Config.Property.DELTA_WEIGHTS));

        this.genes = new LinkedList<Synapse>();
        this.fitness = 0.0;
        this.maxNeuron =  Constants.INPUTS + Constants.OUTPUTS - 1;
        this.globalRank = 0;
        this.step_size = new Double(Jota.get().getConfig().get(Config.Property.STEP_SIZE));
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

    public LinkedList<Synapse> getGenes() { return genes; }

}
