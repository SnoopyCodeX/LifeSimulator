package com.github.jotask.neat.config;

import com.badlogic.gdx.files.FileHandle;
import com.github.jotask.neat.jneat.util.JException;

import java.io.IOException;
import java.util.Properties;

/**
 * LoadConfig
 *
 * @author Jose Vives Iznardo
 * @since 22/03/2017
 */
public class LoadConfig {

    public static Config loadConfig(final String f) throws JException, IOException{

        FileHandle file = new FileHandle(f);

        if(!file.exists()){
            throw new JException("File not exist");
        }

        final Properties properties = new Properties();
        properties.load(file.read());

        return new Config(properties);

    }

    public static Config loadDefault(){

        final Properties properties = new Properties();
        properties.setProperty(Config.Property.POPULATION.name(), String.valueOf(Default.POPULATION));
        properties.setProperty(Config.Property.STALE_SPECIES.name(), String.valueOf(Default.STALE_SPECIES));
        properties.setProperty(Config.Property.DELTA_DISJOINT.name(), String.valueOf(Default.DELTA_DISJOINT));
        properties.setProperty(Config.Property.DELTA_WEIGHTS.name(), String.valueOf(Default.DELTA_WEIGHTS));
        properties.setProperty(Config.Property.DELTA_THRESHOLD.name(), String.valueOf(Default.DELTA_THRESHOLD));
        properties.setProperty(Config.Property.MUTATION.name(), String.valueOf(Default.MUTATION));
        properties.setProperty(Config.Property.CONN_MUTATION.name(), String.valueOf(Default.CONN_MUTATION));
        properties.setProperty(Config.Property.LINK_MUTATION.name(), String.valueOf(Default.LINK_MUTATION));
        properties.setProperty(Config.Property.BIAS_MUTATION.name(), String.valueOf(Default.BIAS_MUTATION));
        properties.setProperty(Config.Property.NODE_MUTATION.name(), String.valueOf(Default.NODE_MUTATION));
        properties.setProperty(Config.Property.ENABLE_MUTATION.name(), String.valueOf(Default.ENABLE_MUTATION));
        properties.setProperty(Config.Property.DISABLE_MUTATION.name(), String.valueOf(Default.DISABLE_MUTATION));
        properties.setProperty(Config.Property.STEP_SIZE.name(), String.valueOf(Default.STEP_SIZE));
        properties.setProperty(Config.Property.PERTURBATION.name(), String.valueOf(Default.PERTURBATION));
        properties.setProperty(Config.Property.CROSSOVER.name(), String.valueOf(Default.CROSSOVER));
        properties.setProperty(Config.Property.THRESHOLD.name(), String.valueOf(Default.THRESHOLD));
        properties.setProperty(Config.Property.INIT_TIME.name(), String.valueOf(Default.INIT_TIME));
        properties.setProperty(Config.Property.PENALIZATION_DISTANCE.name(), String.valueOf(Default.PENALIZATION_DISTANCE));
        properties.setProperty(Config.Property.PENALIZATION_VELOCITY.name(), String.valueOf(Default.PENALIZATION_VELOCITY));
        properties.setProperty(Config.Property.PENALIZATION_HITS.name(), String.valueOf(Default.PENALIZATION_HITS));

        return new Config(properties);

    }

}
