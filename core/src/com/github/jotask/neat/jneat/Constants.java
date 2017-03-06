package com.github.jotask.neat.jneat;

/**
 * Constants
 *
 * @author Jose Vives Iznardo
 * @since 02/03/2017
 */
public class Constants {


    // Enemies TIME
    public static final float INIT_TIME = 1f;

    public static final boolean DRAW = true;

    // Population
    public static final int POPULATION    = 50;
    public static final int STALE_SPECIES = 15;
    public static final int INPUTS        = Util.Inputs.values().length;
    public static final int OUTPUTS       = Util.Outputs.values().length;

    public static final double DELTA_DISJOINT  = 2.0;
    public static final double DELTA_WEIGHTS   = 0.4;
    public static final double DELTA_THRESHOLD = 1.0;

    public static final float CONN_MUTATION    = .25f;
    public static final float LINK_MUTATION    = .2f;
    public static final float BIAS_MUTATION    = .4f;
    public static final float NODE_MUTATION    = .5f;
    public static final float ENABLE_MUTATION  = .2f;
    public static final float DISABLE_MUTATION = .4f;
    public static final float STEP_SIZE        = .1f;
    public static final float PERTURBATION     = .9f;
    public static final float CROSSOVER        = .75f;

    // JNeat

    public static final String FILE = "data/genome.json";
    public static final float THRESHOLD = .5f;

}
