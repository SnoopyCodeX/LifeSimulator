package com.github.jotask.neat.jneat.util;

/**
 * Ref
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Ref {

    public enum Inputs { enemy_x, enemy_y, player_x, player_y, bias }

    public enum Outputs{ left, right, up, down }

    public static final int INPUTS = Inputs.values().length;
    public static final int OUTPUTS = Outputs.values().length;

    public static final int POPULATION    = 50;
    public static final int STALE_SPECIES = 15;

    public static final double DELTA_DISJOINT  = 2.0;
    public static final double DELTA_WEIGHTS   = 0.4;
    public static final double DELTA_THRESHOLD = 1.0;

    public static final double CONN_MUTATION    = 0.25;
    public static final double LINK_MUTATION    = 2.0;
    public static final double BIAS_MUTATION    = 0.4;
    public static final double NODE_MUTATION    = 0.5;
    public static final double ENABLE_MUTATION  = 0.2;
    public static final double DISABLE_MUTATION = 0.4;
    public static final double STEP_SIZE        = 0.1;
    public static final double PERTURBATION     = 0.9;
    public static final double CROSSOVER        = 0.75;

    public static final double THRESHOLD = 0.5;

    public static final float INIT_TIME = 1f;

}
