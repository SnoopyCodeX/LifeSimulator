package com.github.jotask.neat.jneat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import static com.github.jotask.neat.jneat.NeatUtil.isOutput;

// A synapse is a link between two neurons
public class Synapse implements Json.Serializable, Comparable<Synapse>{

    public int input = 0;
    public int output = 0;
    public double weight = 0.0;
    public boolean enabled = true;
    public int innovation = 0;

    public Synapse(){};

    @SuppressWarnings("MethodDoesntCallSuperMethod")
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

    @Override
    public void write(Json json) {
        json.writeValue("input", input, Integer.class);
        json.writeValue("output", output, Integer.class);
        json.writeValue("weight", weight, Double.class);
        json.writeValue("enabled", enabled, Boolean.class);
        json.writeValue("innovation", innovation, Integer.class);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    @Override
    public int compareTo(Synapse other) {
        return this.output - other.output;
    }
}
