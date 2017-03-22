package com.github.jotask.neat.jneat.util;

import com.github.jotask.neat.util.JRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
        return (id < Constants.INPUTS);
    }

    /**
     * Know if a id for a neuron is an Output neuron
     * @param id the id to check
     * @return if the id corresponds to an Output neuron
     */
    public static boolean isOutput(final int id){ return ((id >= Constants.INPUTS) && (id < Constants.INPUTS + Constants.OUTPUTS)); }

    /**
     * Know if a id for a neuron is an Hidden neuron
     * @param id the id to check
     * @return if the id corresponds to a Hidden neuron
     */
    public static boolean isHidden(final int id){ return (id >= Constants.INPUTS + Constants.OUTPUTS); }

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


}
