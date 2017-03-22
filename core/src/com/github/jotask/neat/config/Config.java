package com.github.jotask.neat.config;

import java.util.Properties;

/**
 * Config
 *
 * @author Jose Vives Iznardo
 * @since 22/03/2017
 */
public class Config {

    public enum Property {
        POPULATION,
        STALE_SPECIES,
        DELTA_DISJOINT,
        DELTA_WEIGHTS,
        DELTA_THRESHOLD,
        MUTATION,
        CONN_MUTATION,
        LINK_MUTATION,
        BIAS_MUTATION,
        NODE_MUTATION,
        ENABLE_MUTATION,
        DISABLE_MUTATION,
        STEP_SIZE,
        PERTURBATION,
        CROSSOVER,
        THRESHOLD,
        INIT_TIME
    }

    private final Properties properties;

    Config(Properties properties) { this.properties = properties; }

    public String get(final Property property){ return this.properties.getProperty(property.name()); }

}
