package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Hidden;
import com.github.jotask.neat.jneat.neurons.Input;
import com.github.jotask.neat.jneat.neurons.Neuron;
import com.github.jotask.neat.jneat.neurons.Output;

import java.util.*;

import static com.github.jotask.neat.jneat.Population.*;

/**
 * Network
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
public class Network {

    final HashMap<Integer, Neuron> network;

    public Network(List<Synapse> genes){

        network = new HashMap<Integer, Neuron>();

        for (int i = 0; i < INPUTS; i++){
            network.put(i, new Input(i));
        }

        for (int i = 0; i < OUTPUTS; i++){
            network.put(INPUTS + i, new Output(INPUTS + i));
        }

        Collections.sort(genes, new Comparator<Synapse>() {
            @Override
            public int compare(final Synapse o1, final Synapse o2) { return o1.output - o2.output; }
        });

        for (final Synapse gene : genes) {

            if (gene.enabled) {

                if (!network.containsKey(gene.output))
                    network.put(gene.output, new Hidden(gene.output));

                final Neuron neuron = network.get(gene.output);
                neuron.inputs.add(gene);

                if (!network.containsKey(gene.input))
                    network.put(gene.input, new Hidden(gene.input));

            }

        }
    }

    public void setInputs(double[] input){
        for (int i = 0; i < INPUTS; i++)
            this.getInput(i).value = input[i];
    }

    public double[] getOutputs(){
        final double[] output = new double[OUTPUTS];
        for (int i = 0; i < OUTPUTS; i++)
            output[i] = this.getOutput(i).value;
        return output;
    }

    public void evaluate() {

        for (final Map.Entry<Integer, Neuron> entry : network.entrySet()) {

            final Neuron neuron = entry.getValue();

            if(!(neuron instanceof Hidden)){
                continue;
            }

            double sum = 0.0;
            for (final Synapse incoming : neuron.inputs) {
                final Neuron other = network.get(incoming.input);
                sum += incoming.weight * other.value;
            }

            if (!neuron.inputs.isEmpty())
                neuron.value = Neuron.sigmoid(sum);
        }

        for (final Map.Entry<Integer, Neuron> entry : network.entrySet()) {
            if (entry.getKey() < INPUTS || entry.getKey() >= INPUTS + OUTPUTS)
                continue;
            final Neuron neuron = entry.getValue();
            double sum = 0.0;
            for (final Synapse incoming : neuron.inputs) {
                final Neuron other = network.get(incoming.input);
                sum += incoming.weight * other.value;
            }

            if (!neuron.inputs.isEmpty())
                neuron.value = Neuron.sigmoid(sum);
        }

    }

    private Neuron getOutput(final int i){
        final int id = INPUTS + i;
        if(!isOutput(id)){
            throw new RuntimeException("IS NOT OUTPUT: " + id);
        }
        Neuron n = this.network.get(id);
        if(n instanceof Output){
            return n;
        }
        throw new RuntimeException("IS NOT OUTPUT: " + id);
    }

    private Neuron getInput(final int i){
        if(!isInput(i)){
            throw new RuntimeException("IS NOT INPUT: " + i);
        }
        Neuron n = this.network.get(i);
        if(n instanceof Input) {
            return n;
        }
        throw new RuntimeException("IS NOT INPUT: " + i);
    }

    public Set<Map.Entry<Integer, Neuron>> entrySet() { return this.network.entrySet(); }

}
