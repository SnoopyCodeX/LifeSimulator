package com.github.jotask.neat.config;

/**
 * Default
 *
 * @author Jose Vives Iznardo
 * @since 22/03/2017
 */
class Default {

    public static final int POPULATION    = 300;
    public static final int STALE_SPECIES = 15;

    public static final double DELTA_DISJOINT  = 2.0;
    public static final double DELTA_WEIGHTS   = 0.4;
    public static final double DELTA_THRESHOLD = 1.0;

    public static final float MUTATION = 1f;

    public static final float CONN_MUTATION    = .25f;
    public static final float LINK_MUTATION    = 1f;
    public static final float BIAS_MUTATION    = .1f;
    public static final float NODE_MUTATION    = .50f;
    public static final float ENABLE_MUTATION  = .2f;
    public static final float DISABLE_MUTATION = .4f;
    public static final float STEP_SIZE        = .1f;
    public static final float PERTURBATION     = .9f;
    public static final float CROSSOVER        = .75f;

    public static final double THRESHOLD = 0.0f;

    public static final float INIT_TIME = 1f;
    public static final float TIME_INCREASE = .5f;
    public static final int INCREASE_EACH_GENERATION = 100;

    public static final float PENALIZATION_DISTANCE = 1.5f;
    public static final float PENALIZATION_VELOCITY = 20f;
    public static final float PENALIZATION_HITS = 1.5f;

}
