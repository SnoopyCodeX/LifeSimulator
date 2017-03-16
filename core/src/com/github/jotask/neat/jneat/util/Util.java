package com.github.jotask.neat.jneat.util;

import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Util
 * Util class for utils method for global uses
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public final class Util {

    /**
     * Private constructor
     * With this we avoid to create instances of this class
     */
    private Util(){}

    /**
     * Know if a id for a neuron is an Input neuron
     * @param id the id to check
     * @return if the id corresponds to an Input neuron
     */
    public static boolean isInput(final int id){
        return (id < Ref.INPUTS);
    }

    /**
     * Know if a id for a neuron is an Output neuron
     * @param id the id to check
     * @return if the id corresponds to an Output neuron
     */
    public static boolean isOutput(final int id){ return ((id >= Ref.INPUTS) && (id < Ref.INPUTS + Ref.OUTPUTS)); }

    /**
     * Know if a id for a neuron is an Hidden neuron
     * @param id the id to check
     * @return if the id corresponds to a Hidden neuron
     */
    public static boolean isHidden(final int id){ return (id >= Ref.INPUTS + Ref.OUTPUTS); }

    /**
     *Check if the output value from a neuron is enough to move the entity
     * @param value the value to check
     * @return if is enough to move the entity
     */
    public static boolean threshold(double value){ return (value > Ref.THRESHOLD); }

    /**
     * Limit on double number to only contain n decimals
     * @param number        the number to reduce decimals
     * @param decimals      how many decimals you want on the final number
     * @return              the new number with n decimals values
     */
    public static Double limitDecimals(Double number, int decimals){
        double d = 1D;
        for(int i = 0; i < decimals; i++){
            d *= 10D;
        }
        double n = Math.round(number*d)/d;
        return n;
    }

    public static int indexByProbability(final Collection collection){

        final ArrayList<Integer> numbers = new ArrayList<Integer>();
        for(int i = 0; i < collection.size(); i++) {
            numbers.add(i);
        }

        final ArrayList<Integer> other = new ArrayList<Integer>();
        Collections.reverse(numbers);
        for(int i = 0; i < numbers.size(); i++){
            for(int j = 0; j < i + 1; j++){
                other.add(numbers.get(i));
            }
        }

        int r = JRandom.randomIndex(other);

        return other.get(r);

    }

    public static void main(String[] args) {
        final ArrayList<Integer> numbers = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++){
            numbers.add(i);
        }

        final HashMap<Integer, Integer> probs = new HashMap<Integer, Integer>();
        for(int i = 0; i < numbers.size(); i++){
            probs.put(i, 0);
        }

        System.out.println(probs.toString());
        for(int i = 0; i < 10000; i++){
            int tmp = indexByProbability(numbers);
            int v = probs.get(tmp) + 1;
            probs.put(tmp, v);
        }
        System.out.println(probs.toString());

    }


}
