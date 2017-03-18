package com.github.jotask.neat.jneat.genetics;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Specie
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class Specie implements Json.Serializable{

    public Genome genome;
    public double topFitness;
    public double averageFitness;
    public int staleness;

    Specie(final Genome genome) {
        this.genome = genome;
        this.topFitness = 0.0;
        this.averageFitness = 0.0;
        this.staleness = 0;
    }

    public void calculateAverageFitness() {
        double total = 0.0;
        total += genome.globalRank;
        this.averageFitness = total;
    }

    @Override
    public void write(Json json) {
        json.writeValue("topFitness", this.topFitness);
        json.writeValue("averageFitness", this.averageFitness);
        json.writeValue("staleness", this.staleness);
        json.writeValue("genome", this.genome);
    }

    @Override
    public void read(Json json, JsonValue data) {
        this.topFitness = data.getDouble("topFitness");
        this.averageFitness = data.getDouble("averageFitness");
        this.staleness = data.getInt("staleness");
        this.genome = json.readValue(Genome.class, data.get("genome"));
    }
}
