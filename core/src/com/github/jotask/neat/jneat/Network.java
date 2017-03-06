package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Hidden;
import com.github.jotask.neat.jneat.neurons.Input;
import com.github.jotask.neat.jneat.neurons.Neuron;
import com.github.jotask.neat.jneat.neurons.Output;

import java.util.*;

import static com.github.jotask.neat.jneat.Constants.INPUTS;
import static com.github.jotask.neat.jneat.Constants.OUTPUTS;
import static com.github.jotask.neat.jneat.Util.isInput;
import static com.github.jotask.neat.jneat.Util.isOutput;

/**
 * Network
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Network{

    private final HashMap<Integer, Neuron> network;

    private final LinkedList<Input> inputs;
    private final LinkedList<Hidden> hidden;
    private final LinkedList<Output> outputs;

    public Network(List<Synapse> genes){

        this.network = new HashMap<Integer, Neuron>();

        this.inputs = new LinkedList<Input>();
        this.hidden = new LinkedList<Hidden>();
        this.outputs = new LinkedList<Output>();

        for (int i = 0; i < INPUTS; i++){
            final Input input = new Input(i);
            this.inputs.add(input);
            this.network.put(i, input);
        }

        for (int i = 0; i < OUTPUTS; i++){
            final Output output = new Output(INPUTS + i);
            this.outputs.add(output);
            this.network.put(INPUTS + i, output);
        }

        Collections.sort(genes, new Comparator<Synapse>() {

            @Override
            public int compare(final Synapse o1, final Synapse o2) {
                return o1.getOutput() - o2.getOutput();
            }

        });

        for (final Synapse gene : genes) {

            if (gene.isEnabled()) {

                if (!network.containsKey(gene.getOutput())) {
                    final Hidden hidden = new Hidden(gene.getOutput());
                    this.hidden.add(hidden);
                    this.network.put(gene.getOutput(), hidden);
                }

                final Neuron neuron = network.get(gene.getOutput());
                neuron.getSynapses().add(gene);

                if (!network.containsKey(gene.getInput())){
                    final Hidden hidden = new Hidden(gene.getInput());
                    this.hidden.add(hidden);
                    this.network.put(gene.getInput(), hidden);
                }

            }

        }
    }

    public void setInputs(double[] input){
        for (int i = 0; i < INPUTS; i++){
            this.network.get(i).setValue(input[i]);
        }
    }

    public void evaluate() {

        for (final Map.Entry<Integer, Neuron> entry : network.entrySet()) {

            if (entry.getKey() < INPUTS + OUTPUTS)
                continue;

            final Neuron neuron = entry.getValue();

            double sum = 0.0;
            for (final Synapse incoming : neuron.getSynapses()) {
                final Neuron other = this.network.get(incoming.getInput());
                sum += incoming.getWeight() * other.getValue();
            }

            if (!neuron.getSynapses().isEmpty()) {
                neuron.setValue(Neuron.sigmoid(sum));
            }

        }

        for (final Map.Entry<Integer, Neuron> entry : network.entrySet()) {

            if (entry.getKey() < INPUTS || entry.getKey() >= INPUTS + OUTPUTS) {
                continue;
            }

            final Neuron neuron = entry.getValue();
            double sum = 0.0;
            for (final Synapse incoming : neuron.getSynapses()) {
                final Neuron other = this.network.get(incoming.getInput());
                sum += incoming.getWeight() * other.getValue();
            }

            if (!neuron.getSynapses().isEmpty()) {
                neuron.setValue(Neuron.sigmoid(sum));
            }

        }

    }

    public double[] getOutputs(){
        final double[] output = new double[OUTPUTS];
        for (int i = 0; i < OUTPUTS; i++) {
            output[i] = this.network.get(i).getValue();
        }
        return output;
    }

    private Output getOutput(final int i){
        final int id = INPUTS + i;
        if(!isOutput(id)){
            throw new RuntimeException(id + ": is not output");
        }
        Neuron n = this.network.get(id);
        if(n instanceof Output){
            return (Output) n;
        }
        throw new RuntimeException(id + ": is not output");
    }

    public Hidden getHidden(final int id){
        if(isOutput(id)){
            throw new RuntimeException(id + ": is not hidden");
        }else if(isInput(id)){
            throw new RuntimeException(id + ": is not hidden");
        }
        Neuron n = this.network.get(id);
        if(!(n instanceof Hidden)){
            return (Hidden) n;
        }
        throw new RuntimeException(id + ": is not hidden");
    }

    public Neuron getNeuron(final int id){ return this.network.get(id); }

    private Input getInput(final int i){
        if(!isInput(i)){
            throw new RuntimeException(i + ": is not input: " + Constants.INPUTS + " : " + Util.Inputs.values().length);
        }
        Neuron n = this.network.get(i);
        if(n instanceof Input) {
            return (Input) n;
        }
        throw new RuntimeException(i + ": is not input");
    }

    public LinkedList<Input> getInputNeurons(){ return this.inputs; }

    public Neuron getBiasNeuron(){ return this.getInput(Util.Inputs.BIAS.ordinal()); }

    public LinkedList<Output> getOutputsNeurons(){ return this.outputs; }

    public LinkedList<Hidden> getHiddenNeurons(){ return this.hidden; }

    public HashMap<Integer, Neuron> getNetwork() { return network; }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Network clone");
    }

}
