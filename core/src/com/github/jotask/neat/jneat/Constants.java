package com.github.jotask.neat.jneat;

import com.github.jotask.neat.jneat.neurons.Output;

/**
 * Constants
 *
 * @author Jose Vives Iznardo
 * @since 01/03/2017
 */
public class Constants {

    // Enemies TIME
    public static final float INIT_TIME = 1f;

    // Population
    public static final int POPULATION    = 50;
    public static final int STALE_SPECIES = 15;
    public static final int INPUTS        = 4;
    public static final int OUTPUTS       = Output.OUTPUT.values().length;

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

    // JNeat

    public static final String FILE = "data/genome.json";
    public static final float THRESHOLD = .75f;

}
