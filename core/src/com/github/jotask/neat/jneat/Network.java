package com.github.jotask.neat.jneat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.jotask.neat.jneat.neurons.Hidden;
import com.github.jotask.neat.jneat.neurons.Input;
import com.github.jotask.neat.jneat.neurons.Neuron;
import com.github.jotask.neat.jneat.neurons.Output;

import java.util.*;

import static com.github.jotask.neat.jneat.Constants.INPUTS;
import static com.github.jotask.neat.jneat.Constants.OUTPUTS;
import static com.github.jotask.neat.jneat.NeatUtil.isOutput;
import static com.github.jotask.neat.jneat.NeatUtil.isInput;

/**
 * Network
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
public class Network implements Json.Serializable{

    final HashMap<Integer, Neuron> network;

    public Network(List<Synapse> genes){

        network = new HashMap<Integer, Neuron>();

        for (int i = 0; i < INPUTS; i++){
            network.put(i, new Input(i));
        }

        for (int i = 0; i < OUTPUTS; i++){
            network.put(INPUTS + i, new Output(INPUTS + i));
        }

        Collections.sort(genes);

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

    public Neuron getHidden(final int id){
        if(isOutput(id)){
            throw new RuntimeException("IS OUTPUT: " + id);
        }else if(isInput(id)){
            throw new RuntimeException("IS Input: " + id);
        }
        Neuron n = this.network.get(id);
        if(!(n instanceof Hidden)){
            return n;
        }
        throw new RuntimeException("IS NOT HIDDEN: " + id);
    }

    public Neuron getNeuron(final int id){ return this.network.get(id); }

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

    public LinkedList<Neuron> getOutputsNeurons(){
        final LinkedList<Neuron> out = new LinkedList<Neuron>();
        for (int i = 0; i < OUTPUTS; i++){
            out.add(this.network.get(INPUTS + i));
        }

        for(Neuron n: out){
            if(!(n instanceof Output)){
                throw new RuntimeException("All nodes they aren't outputs");
            }
        }

        return out;
    }

    public Set<Map.Entry<Integer, Neuron>> entrySet() { return this.network.entrySet(); }

    @Override
    public void write(Json json) { }

    @Override
    public void read(Json json, JsonValue jsonData) { }
}
