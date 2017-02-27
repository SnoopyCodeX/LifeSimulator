package com.github.jotask.neat.jneat;

import java.util.ArrayList;
import java.util.List;

import static com.github.jotask.neat.jneat.JNeat.random;
import static com.github.jotask.neat.jneat.Population.*;

public class Genome {

    public final List<Synapse> genes = new ArrayList<Synapse>();
    public double fitness = 0.0;
    public int maxNeuron = 0;
    public int globalRank = 0;
    public final double[] mutationRates = new double[] { CONN_MUTATION, LINK_MUTATION, BIAS_MUTATION, NODE_MUTATION, ENABLE_MUTATION, DISABLE_MUTATION, STEP_SIZE };
    public Network network = null;

    public Genome() {}

    @Override
    public Genome clone() {
        final Genome genome = new Genome();

        for (final Synapse gene : genes)
            genome.genes.add(gene.clone());

        genome.maxNeuron = maxNeuron;

        for (int i = 0; i < mutationRates.length; ++i)
            genome.mutationRates[i] = mutationRates[i];

        return genome;
    }

    public boolean containsLink(final Synapse link) {
        for (final Synapse gene : genes)
            if (gene.input == link.input && gene.output == link.output)
                return true;
        return false;
    }

    public double disjoint(final Genome genome) {
        double disjointGenes = 0.0;
        search: for (final Synapse gene : genes) {
            for (final Synapse otherGene : genome.genes)
                if (gene.innovation == otherGene.innovation)
                    continue search;
            ++disjointGenes;
        }
        return disjointGenes / Math.max(genes.size(), genome.genes.size());
    }

    /**
     * Evaluate the network
     * @param input the inputs from the entity
     * @return outputs evaluated from the network
     */
    public double[] evaluateNetwork(final double[] input) {
        network.setInputs(input);
        network.evaluate();
        return network.getOutputs();
    }

    /**
     * Generate the Network
     */
    public void generateNetwork() { network = new Network(this.genes); }

    /**
     * Check if two outputs are connected
     */
    private void check(){
        for(Synapse s: genes){
            if(s.isOut()){
                throw new RuntimeException("Two outputs connected");
            }
        }
    }

    public void mutate() {

        // FIXME here is were the outputs neurons connect

        for (int i = 0; i < 7; ++i)
            mutationRates[i] *= random.nextBoolean() ? 0.95 : 1.05263;

        if (random.nextDouble() < mutationRates[0])
            mutatePoint();

        double prob = mutationRates[1];
        while (prob > 0) {
            if (random.nextDouble() < prob)
                mutateLink(false);
            --prob;
        }

        prob = mutationRates[2];
        while (prob > 0) {
            if (random.nextDouble() < prob)
                mutateLink(true);
            --prob;
        }

        prob = mutationRates[3];
        while (prob > 0) {
            if (random.nextDouble() < prob)
                mutateNode();
            --prob;
        }

        prob = mutationRates[4];
        while (prob > 0) {
            if (random.nextDouble() < prob)
                mutateEnableDisable(true);
            --prob;
        }

        prob = mutationRates[5];
        while (prob > 0) {
            if (random.nextDouble() < prob)
                mutateEnableDisable(false);
            --prob;
        }
    }

    public void mutateEnableDisable(final boolean enable) {

        final List<Synapse> candidates = new ArrayList<Synapse>();
        for (final Synapse gene : genes)
            if (gene.enabled != enable)
                candidates.add(gene);

        if (candidates.isEmpty())
            return;

        final Synapse gene = candidates.get(random.nextInt(candidates.size()));
        gene.enabled = !gene.enabled;

    }

    public void mutateLink(final boolean forceBias) {

        int neuron1;
        int neuron2;

        do{
            neuron1 = randomNeuron(false, true);
            neuron2 = randomNeuron(true, false);
        }while((isOutput(neuron1) && isOutput(neuron2)));

        final Synapse newLink = new Synapse();

            newLink.input = neuron1;
            newLink.output = neuron2;

        if (forceBias)
            newLink.input = INPUTS - 1;

        if (containsLink(newLink))
            return;

        newLink.innovation = ++Population.innovation;
        newLink.weight = random.nextDouble() * 4.0 - 2.0;

        genes.add(newLink);

    }

    public void mutateNode() {

        // FIXME improve
        check();
//        System.out.println("enter");

        if (genes.isEmpty())
            return;

        final Synapse gene = genes.get(random.nextInt(genes.size()));

        if (!gene.enabled)
            return;

        gene.enabled = false;

        ++maxNeuron;

        if(isOutput(maxNeuron)){
//            System.out.println(gene.input + " : " + gene.output);
            return;
        }

        final Synapse gene1 = gene.clone();
        gene1.output = maxNeuron;
        gene1.weight = 1.0;
        gene1.innovation = ++Population.innovation;
        gene1.enabled = true;
        genes.add(gene1);

        final Synapse gene2 = gene.clone();
        gene2.input = maxNeuron;
        gene2.innovation = ++Population.innovation;
        gene2.enabled = true;
        genes.add(gene2);

        check();
//        System.out.println("exit");

    }

    public void mutatePoint() {
        for (final Synapse gene : genes)
            if (random.nextDouble() < PERTURBATION)
                gene.weight += random.nextDouble() * mutationRates[6] * 2.0
                        - mutationRates[6];
            else
                gene.weight = random.nextDouble() * 4.0 - 2.0;
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
            if ((!nonInput || gene.input >= INPUTS) && (!nonOutput || gene.input >= INPUTS + OUTPUTS))
                neurons.add(gene.input);
            if ((!nonInput || gene.output >= INPUTS) && (!nonOutput || gene.output >= INPUTS + OUTPUTS))
                neurons.add(gene.output);
        }

        return neurons.get(random.nextInt(neurons.size()));
    }

    public boolean sameSpecies(final Genome genome) {
        final double dd = DELTA_DISJOINT * disjoint(genome);
        final double dw = DELTA_WEIGHTS * weights(genome);
        return dd + dw < DELTA_THRESHOLD;
    }

    public double weights(final Genome genome) {
        double sum = 0.0;
        double coincident = 0.0;
        search: for (final Synapse gene : genes)
            for (final Synapse otherGene : genome.genes)
                if (gene.innovation == otherGene.innovation) {
                    sum += Math.abs(gene.weight - otherGene.weight);
                    ++coincident;
                    continue search;
                }
        return sum / coincident;
    }
}
