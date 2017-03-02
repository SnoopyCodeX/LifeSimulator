package com.github.jotask.neat.jneat;

import static com.github.jotask.neat.jneat.Constants.INPUTS;
import static com.github.jotask.neat.jneat.Constants.OUTPUTS;

/**
 * NeatUtil
 *
 * @author Jose Vives Iznardo
 * @since 01/03/2017
 */
public class NeatUtil {

    public static boolean isInput(final int id){
        return id < INPUTS;
    }

    public static boolean isOutput(final int id){
        return id >= INPUTS && id < INPUTS + OUTPUTS;
    }

}
