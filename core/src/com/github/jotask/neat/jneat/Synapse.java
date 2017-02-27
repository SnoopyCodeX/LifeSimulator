package com.github.jotask.neat.jneat;

import static com.github.jotask.neat.jneat.Population.isOutput;

// A synapse is a link between two neurons
public class Synapse{

    public int input = 0;
    public int output = 0;
    public double weight = 0.0;
    public boolean enabled = true;
    public int innovation = 0;

    @Override
    public Synapse clone() {
        final Synapse synapse = new Synapse();
        synapse.input = this.input;
        synapse.output = this.output;
        synapse.weight = this.weight;
        synapse.enabled = this.enabled;
        synapse.innovation = this.innovation;
        return synapse;
    }

    public boolean isOut(){ return(isOutput(this.input) && isOutput(this.output)); }

}
