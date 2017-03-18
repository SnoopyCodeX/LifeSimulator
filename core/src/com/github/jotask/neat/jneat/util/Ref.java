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

    public static final int POPULATION    = 10;
    public static final int STALE_SPECIES = 15;

    public static final double DELTA_DISJOINT  = 2.0;
    public static final double DELTA_WEIGHTS   = 0.4;
    public static final double DELTA_THRESHOLD = 1.0;

    public static final float MUTATION = .75f;

    private static final int MAX_HIDDEN_NEURONS = 15;

    public static final int MAX_NEURONS;
    static{ MAX_NEURONS = INPUTS + OUTPUTS + MAX_HIDDEN_NEURONS; }

    public static final float CONN_MUTATION    = 1f;
    public static final float LINK_MUTATION    = 1f;
    public static final float BIAS_MUTATION    = 1f;
    public static final float NODE_MUTATION    = 1f;
    public static final float ENABLE_MUTATION  = .002f;
    public static final float DISABLE_MUTATION = .004f;
    public static final float STEP_SIZE        = .1f;
    public static final float PERTURBATION     = .9f;
    public static final float CROSSOVER        = 1f;

    public static final float THRESHOLD = .5f;

    public static final float INIT_TIME = 1f;

    public static final boolean LOAD = false;

    // FIXME two inputs synapses are connected
    // FIXME synapses disabled are innabled

}
