package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Neuron;

/**
 * Synapse
 *
 * A Synapse is a link between two neurons
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Synapse{

    private int input;
    private int output;

    private double weight;
    private boolean enabled;

    private int innovation;

    public Synapse(final Integer input, final Integer output) {
        this.input = input;
        this.output = output;
        this.weight = 0f;
        this.enabled = true;
        this.innovation = 0;
    }

    Synapse(final Synapse syn) {
        this.input = syn.input;
        this.output = syn.output;
        this.weight = syn.weight;
        this.enabled = syn.enabled;
        this.innovation = syn.innovation;
    }

    public int getInput() { return input; }

    public int getOutput() { return output; }

    public double getWeight() { return weight; }

    void setWeight(double weight) { this.weight = weight; }

    void addWeight(double toAdd){
        double tmp =(getWeight() + toAdd);
        this.setWeight(tmp);
    }

    int getInnovation() { return innovation; }

    public void setInnovation(int innovation) { this.innovation = innovation; }

    public boolean isEnabled() { return enabled; }

    void setEnabled(boolean enabled) { this.enabled = enabled; }

    public void setInput(final int input) { this.input = input; }

    public void setOutput(final int output) { this.output = output; }

    @Override
    protected Synapse clone() throws CloneNotSupportedException {
        throw new RuntimeException("Synapse clone");
    }

    public static final boolean inputs(final Synapse syn){
        return (Neuron.isInput(syn.getInput()) && Neuron.isInput(syn.getOutput()));
    }

    public static final boolean outputs(final Synapse syn){
        return (Neuron.isOutput(syn.getInput()) && Neuron.isOutput(syn.getOutput()));
    }

}
