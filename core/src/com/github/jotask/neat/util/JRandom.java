package com.github.jotask.neat.util;

import com.badlogic.gdx.math.Vector2;

import java.util.Collection;
import java.util.Random;

/**
 * JRandom
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public final class JRandom {

    private JRandom(){}

    public static final Random random = new Random();

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
        final float WIDTH = Constant.WORLD_WIDTH / 2f;
        final float HEIGHT = Constant.WORLD_HEIGHT / 2f;
        float x = random(-WIDTH, WIDTH);
        float y = random(-HEIGHT, HEIGHT);
        return new Vector2(x, y);
    }

    public static Vector2 randomPositionPlayer(){
        final float offset = 5f;
        final float WIDTH = Constant.WORLD_WIDTH / 2f;
        final float HEIGHT = Constant.WORLD_HEIGHT / 2f;
        float x = random(-WIDTH + offset, WIDTH - offset);
        float y = random(-HEIGHT + offset, HEIGHT - offset);
        return new Vector2(x, y);
    }

    public static int randomIndex(final Collection c){ return random.nextInt(c.size()); }

}
