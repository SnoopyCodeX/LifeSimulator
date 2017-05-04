package com.github.jotask.neat.jneat.network;

import com.github.jotask.neat.jneat.genetics.Synapse;
import com.github.jotask.neat.jneat.util.Constants;

import java.util.*;

/**
 * Network
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Network {

    public Map<Integer, Neuron> neurons = null;

    public Network(final LinkedList<Synapse> genes) {

        this.neurons = new HashMap<Integer, Neuron>();
        for(int i = 0; i < Constants.INPUTS; i++){
            neurons.put(i, new Neuron(i));
        }
        for(int i = 0; i < Constants.OUTPUTS; i++){
            final int id = Constants.INPUTS + i;
            this.neurons.put(id, new Neuron(id));
        }
        Collections.sort(genes, new Comparator<Synapse>() {

            @Override
            public int compare(final Synapse a, final Synapse b) {
                // table.sort(genome.genes, function (a,b)
                // return (a.out < b.out) end
                return a.getOutput() - b.getOutput();
            }
        });

        for(final Synapse gene: genes){
            if(gene.isEnabled()){
                if(!(this.neurons.containsKey(gene.getOutput()))){
                    neurons.put(gene.getOutput(), new Neuron(gene.getOutput()));
                }
                final Neuron neuron = neurons.get(gene.getOutput());
                neuron.getInputs().add(gene);
                if(!(this.neurons.containsKey(gene.getInput()))){
                    this.neurons.put(gene.getInput(), new Neuron(gene.getInput()));
                }
            }
        }

    }

    public double[] evaluate(final double[] inputs){

        for(int i = 0; i < Constants.INPUTS; i++){
            neurons.get(i).setValue(inputs[i]);
        }

        for(final Map.Entry<Integer, Neuron> entry: neurons.entrySet()){

//            if(entry.getKey() < Constants.INPUTS + Constants.OUTPUTS){
//                continue;
//            }

            final Neuron neuron = entry.getValue();

            double sum = 0.0;
            for(final Synapse incoming: neuron.getInputs()){
                final Neuron other = neurons.get(incoming.getInput());
                sum += incoming.getWeight() * other.getValue();

                if(!neuron.getInputs().isEmpty()){
                    neuron.setValue(Neuron.sigmoid(sum));
                }
            }
        }

        final double[] output = new double[Constants.OUTPUTS];
        for(int i = 0; i < Constants.OUTPUTS; i++){
            output[i] = neurons.get(Constants.INPUTS + i).getValue();
        }
        return output;
    }

}
