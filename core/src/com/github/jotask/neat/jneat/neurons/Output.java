package com.github.jotask.neat.jneat.neurons;

/**
 * Output
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
public class Output extends Neuron{

    public enum OUTPUT { LEFT, RIGHT, UP, DOWN }

    public Output(int id) {
        super(id, Type.OUTPUT);
//        System.out.println("Output: " + id);
    }

}
