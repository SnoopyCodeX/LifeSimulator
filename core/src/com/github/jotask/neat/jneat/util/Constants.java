package com.github.jotask.neat.jneat.util;

/**
 * Constants
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Constants {

    public enum Inputs { enemy_x, enemy_y, player_x, player_y, bias }

    public enum Outputs{ left, right, up, down, w_left, w_right, w_up, w_down }

    public static final int INPUTS = Inputs.values().length;
    public static final int OUTPUTS = Outputs.values().length;

    public static final boolean SAVE = false;
    public static final boolean LOAD = true;

}
