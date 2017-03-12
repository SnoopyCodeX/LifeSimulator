package com.github.jotask.neat.jneat.genetics;

/**
 * Synapse
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Synapse {

    private int input;
    private int output;

    private double weight;

    private boolean enabled;
    private int innovation;

    Synapse() {
        this.input = 0;
        this.output = 0;
        this.weight = 0.0;
        this.enabled = true;
        this.innovation = 0;
    }

    Synapse(final Synapse synapse) {
        this.input = synapse.input;
        this.output = synapse.output;
        this.weight = synapse.weight;
        this.enabled = synapse.enabled;
        this.innovation = synapse.innovation;
    }

    public int getInput() {
        return input;
    }

    void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    void setOutput(int output) {
        this.output = output;
    }

    public double getWeight() {
        return weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    int getInnovation() {
        return innovation;
    }

    void setInnovation(int innovation) {
        this.innovation = innovation;
    }

}
