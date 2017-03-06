package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Neuron;
import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.List;

import static com.github.jotask.neat.jneat.Constants.BIAS_MUTATION;
import static com.github.jotask.neat.jneat.Constants.CONN_MUTATION;
import static com.github.jotask.neat.jneat.Constants.DELTA_DISJOINT;
import static com.github.jotask.neat.jneat.Constants.DELTA_THRESHOLD;
import static com.github.jotask.neat.jneat.Constants.DELTA_WEIGHTS;
import static com.github.jotask.neat.jneat.Constants.DISABLE_MUTATION;
import static com.github.jotask.neat.jneat.Constants.ENABLE_MUTATION;
import static com.github.jotask.neat.jneat.Constants.LINK_MUTATION;
import static com.github.jotask.neat.jneat.Constants.NODE_MUTATION;
import static com.github.jotask.neat.jneat.Constants.PERTURBATION;
import static com.github.jotask.neat.jneat.Constants.STEP_SIZE;
import static com.github.jotask.neat.jneat.test.neat.Pool.INPUTS;
import static com.github.jotask.neat.jneat.test.neat.Pool.OUTPUTS;
import static com.github.jotask.neat.jneat.test.neat.Pool.*;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Genome{

    private final List<Synapse> genes;
    private double fitness = 0.0;
    private int globalRank = 0;

    private Network network = null;

    /**
     * Default constructor to initialize variables
     */
    public Genome(){
         this.genes = new ArrayList<Synapse>();
    }

    /**
     * Copy constructor
     * @param genome
     */
    public Genome(final Genome genome){
        this();

        for (final Synapse gene : genes) {
            genome.genes.add(new Synapse(gene));
        }

    }

    public void generateNetwork(){
        this.network = new Network(this.genes);
    }

    public boolean containsLink(final Synapse link) {
        for (final Synapse gene : genes) {
            if (gene.getInput() == link.getInput() && gene.getOutput() == link.getOutput()) {
                return true;
            }
        }
        return false;
    }

    public double disjoint(final Genome genome) {
        double disjointGenes = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes) {
                if (gene.getInnovation() == otherGene.getInnovation()) {
                    continue search;
                }
                disjointGenes++;
            }
        }
        return disjointGenes / Math.max(genes.size(), genome.genes.size());
    }

    public double[] evaluateNetwork(final double[] input) {
        this.network.setInputs(input);
        this.network.evaluate();
        return this.network.getOutputs();
    }

    private void check(){
        for(Synapse s: this.genes){
            if(Synapse.inputs(s))
                throw new RuntimeException("Two inputs connected");
            if(Synapse.outputs(s))
                throw new RuntimeException("Two outputs connected");
        }
    }

    public void mutate() {

        check();
        System.out.println("mutate.enter");

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

        check();
        System.out.println("mutate.exit");

    }

    public void mutateEnableDisable(final boolean enable) {
        check();
        System.out.println("mutateEnableDisable.enter : " + enable);
        final List<Synapse> candidates = new ArrayList<Synapse>();
        for (final Synapse gene : genes){
            if (gene.isEnabled() != enable) {
                candidates.add(gene);
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        final Synapse gene = candidates.get(JRandom.random.nextInt(candidates.size()));
        gene.setEnabled(!gene.isEnabled());
        check();
        System.out.println("mutateEnableDisable.exit : " + enable);

    }

    public void mutateLink(final boolean forceBias) {

        check();
        System.out.println("mutateLink.enter : forceBias = " + forceBias);

        final int neuron1 = randomNeuron(false, true);
        final int neuron2 = randomNeuron(true, false);

        final Synapse newLink = new Synapse(neuron1, neuron2);

        if (forceBias)
            newLink.setInput(INPUTS - 1);

        if (containsLink(newLink)) {
            System.out.println("Link already existed");
            return;
        }

        newLink.setInnovation(++Population.innovation);
        newLink.setWeight(rnd.nextDouble() * 4.0 - 2.0);

        genes.add(newLink);

        check();
        System.out.println("mutateLink.exit : " + forceBias);

    }

    public void mutateNode() {

        check();
        System.out.println("mutateNode.enter");

        if (genes.isEmpty())
            return;

        final Synapse gene = genes.get(JRandom.random.nextInt(genes.size()));

        if (!gene.isEnabled())
            return;

        gene.setEnabled(false);

//        Output out = this.network.getOutputsNeurons().get(JRandom.randomIndex(this.network.getOutputsNeurons()));
        final List<Integer> outputIndexes = this.getOutputIndexes();
        Integer out = outputIndexes.get(JRandom.randomIndex(outputIndexes));

        final int a = gene.getInput();
        final int b = gene.getOutput();

        if( (!Neuron.isOutput(a)) && (!Neuron.isInput(a)) ) {
            final Synapse gene1 = new Synapse(gene);
            gene1.setOutput(out);
            gene1.setWeight(1.0);
            gene1.setInnovation(++Population.innovation);
            gene1.setEnabled(true);
            genes.add(gene1);
        }

        if( (!Neuron.isInput(b)) && (!Neuron.isOutput(b)) ) {
            final Synapse gene2 = new Synapse(gene);
            gene2.setInput(out);
            gene2.setInnovation(++Population.innovation);
            gene2.setEnabled(true);
            genes.add(gene2);
        }

        check();
        System.out.println("mutateNode.exit");

    }

    public void mutatePoint() {
        check();
        System.out.println("mutatePoint.enter");
        for (final Synapse gene : genes) {
            if (JRandom.random() < PERTURBATION) {
                gene.addWeight(JRandom.random.nextDouble() * STEP_SIZE * 2.0 - STEP_SIZE);
            } else {
                gene.setWeight(JRandom.random.nextDouble() * 4.0 - 2.0);
            }
        }
        check();
        System.out.println("mutatePoint.exit");
    }

    public List<Integer> getInputsIndexes(){
        final List<Integer> neurons = new ArrayList<Integer>();
        for (int i = 0; i < INPUTS; i++) {
            neurons.add(i);
        }
        return neurons;
    }

    public List<Integer> getOutputIndexes(){
        final List<Integer> neurons = new ArrayList<Integer>();
        for (int i = 0; i < OUTPUTS; i++) {
            neurons.add(INPUTS + i);
        }
        return neurons;
    }

    public int randomNeuron(final boolean nonInput, final boolean nonOutput) {
        final List<Integer> neurons = new ArrayList<Integer>();

        if (!nonInput) { neurons.addAll(this.getInputsIndexes()); }

        if (!nonOutput) { neurons.addAll(this.getOutputIndexes()); }

        for (final Synapse gene : genes) {
            if ((!nonInput || gene.getInput() >= INPUTS) && (!nonOutput || gene.getInput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getInput());
            }
            if ((!nonInput || gene.getOutput() >= INPUTS) && (!nonOutput || gene.getOutput() >= INPUTS + OUTPUTS)) {
                neurons.add(gene.getOutput());
            }
        }

        Integer integer = neurons.get(JRandom.random.nextInt(neurons.size()));

        return integer;

    }

    public boolean sameSpecies(final Genome genome) {
        final double dd = DELTA_DISJOINT * disjoint(genome);
        final double dw = DELTA_WEIGHTS * weights(genome);
        return dd + dw < DELTA_THRESHOLD;
    }

    public double weights(final Genome genome) {
        double sum = 0.0;
        double coincident = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes) {
                if (gene.getInnovation() == otherGene.getInnovation()) {
                    sum += Math.abs(gene.getWeight() - otherGene.getWeight());
                    coincident++;
                    continue search;
                }
            }
        }
        return sum / coincident;
    }

    // GETTERS AND SETTERS

    public List<Synapse> getGenes() { return genes; }

    public double getFitness() { return fitness; }

    public void setFitness(double fitness) { this.fitness = fitness; }

    public int getGlobalRank() { return globalRank; }

    public void setGlobalRank(int globalRank) { this.globalRank = globalRank; }

    @Override
    public Genome clone() {
        throw new RuntimeException("Genome Clone");
    }

    public Network getNetwork() { return network; }
}
