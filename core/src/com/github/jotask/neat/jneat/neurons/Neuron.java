package com.github.jotask.neat.jneat.neurons;

import com.github.jotask.neat.jneat.Synapse;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron{

    public static double sigmoid(final double x) {
        return 2.0 / (1.0 + Math.exp(-4.9 * x)) - 1.0;
    }

    public enum Type { INPUT, HIDDEN, OUTPUT }

    public final Type type;

    private final int id;

    public double value = 0.0;

    public final List<Synapse> inputs = new ArrayList<Synapse>();

    public Neuron(final int id, final Type type) {
        this.id = id;
        this.type = type;
    }

    public int getId() { return id; }

}
