package com.github.jotask.neat.engine;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * JRandom
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public final class JRandom {

    private JRandom(){}

    static final Random random = new Random();

    public static float random(){ return random.nextFloat(); }

    public static float random(final float start, final float end) { return start + random() * (end - start); }

    public static Vector2 getDir(){
        float start = -1;
        float end = 1;
        float x = random(start, end);
        float y = random(start, end);
        return new Vector2(x, y);
    }

}
