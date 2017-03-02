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

    public static final Random random = new Random(23);

    public static float random(){ return random.nextFloat(); }

    public static float random(final float start, final float end) { return start + random() * (end - start); }

    public static Vector2 getDir(){
        float start = -1;
        float end = 1;
        float x = random(start, end);
        float y = random(start, end);
        return new Vector2(x, y);
    }

    public static Vector2 randomPosition(){
        final float WIDTH = 21f / 2f;
        final float HEIGHT = 11f / 2f;
        float x = random(-WIDTH, WIDTH);
        float y = random(-HEIGHT, HEIGHT);
        return new Vector2(x, y);
    }

    public static Vector2 randomPositionPlayer(){
        final float offset = 5f;
        final float WIDTH = 21f / 2f;
        final float HEIGHT = 11f / 2f;
        float x = random(-WIDTH + offset, WIDTH - offset);
        float y = random(-HEIGHT + offset, HEIGHT - offset);
        return new Vector2(x, y);
    }

}
