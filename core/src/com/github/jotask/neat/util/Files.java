package com.github.jotask.neat.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.github.jotask.neat.jneat.genetics.Population;
import com.github.jotask.neat.jneat.util.Ref;

/**
 * Files
 *
 * @author Jose Vives Iznardo
 * @since 12/03/2017
 */
public abstract class Files {

    static final String FILE = "population";
    static final String EXTENSION = ".json";

    private Files(){}

    public static void save(final Population population){
        final Json json = new Json();
        final FileHandle fileHandle = Gdx.files.local(FILE + EXTENSION);
        final String text = json.prettyPrint(population);
        fileHandle.writeString(text, false);
    }

    public static Population load(){
        FileHandle fileHandle = Gdx.files.local(FILE + EXTENSION);

        if(!Ref.LOAD){
            fileHandle.delete();
        }

        if(!fileHandle.exists()){
            System.out.println("does not exist");
            Population population = new Population();
            population.initialize();
            return population;
        }else{
            return new Json().fromJson(Population.class, fileHandle);
        }
    }

}
