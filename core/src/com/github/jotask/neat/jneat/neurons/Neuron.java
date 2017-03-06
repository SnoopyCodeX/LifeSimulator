package com.github.jotask.neat.jneat.neurons;

import com.github.jotask.neat.jneat.Constants;
import com.github.jotask.neat.jneat.Synapse;

import java.util.LinkedList;

/**
 * Neuron
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public abstract class Neuron {

    public static double sigmoid(final double x) { return 2.0 / (1.0 + Math.exp(-4.9 * x)) - 1.0; }

    private final int id;

    private double value;

    private final LinkedList<Synapse> inputs;

    public Neuron(final int id){
        this.id = id;
        this.inputs = new LinkedList<Synapse>();
    }

    public int getId() { return id; }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

    public LinkedList<Synapse> getSynapses() { return inputs; }

    public static final boolean isInput(final int id){ return (id < Constants.INPUTS); }

    public static final boolean isOutput(final int id){ return (id >= Constants.INPUTS && id < Constants.INPUTS + Constants.OUTPUTS); }

}
