package com.github.jotask.neat.jneat.network;

import com.github.jotask.neat.jneat.genetics.Synapse;

import java.util.LinkedList;

/**
 * Neuron
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
class Neuron {

    static double sigmoid(final double x) {
        return 2.0 / (1.0 + Math.exp(-4.9 * x)) - 1.0;
    }

    private final int id;

    private double value;
    private final LinkedList<Synapse> inputs;

    Neuron(int id) {
        this.id = id;
        this.inputs = new LinkedList<Synapse>();
        this.value = 0.0;
    }

    int getId() {
        return id;
    }

    double getValue() {
        return value;
    }

    void setValue(double value) {
        this.value = value;
    }

    LinkedList<Synapse> getInputs() {
        return inputs;
    }

}
