package com.github.jotask.neat.engine.radar;

/**
 * Radar
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class Radar<T> {

    public static final float SIZE = 7f;

    private T ent;

    public T getEnt() {
        return ent;
    }

    public void setEnt(T ent) {
        if(this.ent != null) {
            System.err.println("enemy is not null");
            return;
        }
        this.ent = ent;
    }

}
