package com.github.jotask.neat.jneat;

/**
 * Util
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Util {

    private Util(){}

    public enum Inputs{
        ENEMY_X, ENEMY_Y, PLAYER_X, PLAYER_Y, BIAS
    }

    public enum Outputs{
        DOWN, UP, LEFT, RIGHT
    }

    public static final boolean isInput(final int id){
        return (id < Constants.INPUTS);
    }

    public static final boolean isOutput(final int id){ return ((id >= Constants.INPUTS) && (id < Constants.INPUTS + Constants.OUTPUTS)); }

    public static boolean threshold(double value){ return (value > Constants.THRESHOLD); }

}
