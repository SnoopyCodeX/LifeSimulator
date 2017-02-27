package com.github.jotask.neat.jneat.neurons;

import com.github.jotask.neat.jneat.Synapse;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron {

    public static double sigmoid(final double x) {
        return 2.0 / (1.0 + Math.exp(-4.9 * x)) - 1.0;
    }

    public enum Type { INPUT, HIDDEN, OUTPUT }

    public final Type type;

    private final int id;

    public double value = 0.0;

    public final List<Synapse> inputs = new ArrayList<Synapse>();
//    public final List<Synapse> outputs = new ArrayList<Synapse>();

//    public double              bias        = 0.0;
//
//    public boolean             updated     = false;
//    public boolean             evalInput   = false;
//    public boolean             evalOutput  = false;
//    public boolean             cacheInput  = false;
//    public boolean             cacheOutput = false;

    public Neuron(final int id, final Type type) {
        this.id = id;
        this.type = type;
    }

//    public boolean connectedInput(){
//        if(isInput(this.id))
//            return true;
//        if(evalInput)
//            return cacheInput;
//        evalInput = true;
//        boolean connectInput = false;
//        for(final Synapse connected: inputs) {
//            if (connected.enabled && connected.input.connectedInput()) {
//                connectInput = true;
//            }
//        }
//        cacheInput = connectInput;
//        return connectInput;
//
//    }

//    public boolean connectedOutput() {
//        if (isOutput(this.id))
//            return true;
//        if (evalOutput)
//            return cacheOutput;
//        evalOutput = true;
//        boolean connectedOutput = false;
//        for(final Synapse connect: outputs){
//            if(connect.enabled && connect.output.connectedOutput()){
//                connectedOutput = true;
//            }
//        }
//        cacheOutput = connectedOutput;
//        return connectedOutput;
//    }

//    public void feedForward(){
//        if(updated)
//            return;
//        updated = true;
//        if(!inputs.isEmpty()){
//            value = bias;
//            for(final Synapse connect: inputs){
//                if(connect.enabled){
//                    value += connect.weight * connect.input.value;
//                }
//            }
//        }
//        for(final Synapse connect: outputs){
//            if(connect.enabled){
//                connect.output.feedForward();
//            }
//        }
//    }

    public int getId() { return id; }


//    public boolean shouldDisplay(){ return connectedInput() && connectedOutput(); }

}
