package com.github.jotask.neat.jneat.network;

import com.github.jotask.neat.jneat.util.Constants;
import com.github.jotask.neat.jneat.genetics.Synapse;

import java.util.*;

import static com.github.jotask.neat.jneat.util.Constants.INPUTS;
import static com.github.jotask.neat.jneat.util.Constants.OUTPUTS;

/**
 * Network
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Network {

    public Map<Integer, Neuron> network = null;

    public Network(final LinkedList<Synapse> genes) {

        this.network = new HashMap<Integer, Neuron>();
        for(int i = 0; i < Constants.INPUTS; i++){
            network.put(i, new Neuron(i));
        }
        for(int i = 0; i < Constants.OUTPUTS; i++){
            final int id = Constants.INPUTS + i;
            this.network.put(id, new Neuron(id));
        }
        Collections.sort(genes, new Comparator<Synapse>() {

            @Override
            public int compare(final Synapse o1, final Synapse o2) {
                return o1.getOutput() - o2.getOutput();
            }
        });

        for(final Synapse gene: genes){
            if(gene.isEnabled()){
                if(!(this.network.containsKey(gene.getOutput()))){
                    network.put(gene.getOutput(), new Neuron(gene.getOutput()));
                }
                final Neuron neuron = network.get(gene.getOutput());
                neuron.getInputs().add(gene);
                if(!(this.network.containsKey(gene.getInput()))){
                    this.network.put(gene.getInput(), new Neuron(gene.getInput()));
                }
            }
        }

    }

    public double[] evaluate(final double[] inputs){

        for(int i = 0; i < Constants.INPUTS; i++){
            network.get(i).setValue(inputs[i]);
        }

        for(final Map.Entry<Integer, Neuron> entry: network.entrySet()){

            if(entry.getKey() < Constants.INPUTS + Constants.OUTPUTS){
                continue;
            }

            final Neuron neuron = entry.getValue();
            double sum = 0.0;

            for(final Synapse incoming: neuron.getInputs()){
                final Neuron other = network.get(incoming.getInput());
                sum += incoming.getWeight() * other.getValue();

                if(!neuron.getInputs().isEmpty()){
                    neuron.setValue(Neuron.sigmoid(sum));
                }
            }
        }

        for(final Map.Entry<Integer, Neuron> entry: network.entrySet()){
            if(entry.getKey() < INPUTS || entry.getKey() >= INPUTS + OUTPUTS){
                continue;
            }
            final Neuron neuron = entry.getValue();
            double sum = 0.0;
            for(final Synapse incoming: neuron.getInputs()){
                final Neuron other = this.network.get(incoming.getInput());
                sum += incoming.getWeight() * other.getValue();
            }
            if(!neuron.getInputs().isEmpty()){
                neuron.setValue(Neuron.sigmoid(sum));
            }
        }

        final double[] output = new double[Constants.OUTPUTS];
        for(int i = 0; i < Constants.OUTPUTS; i++){
            output[i] = network.get(Constants.INPUTS + i).getValue();
        }
        return output;
    }

}
