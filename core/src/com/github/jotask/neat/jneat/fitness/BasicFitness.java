package com.github.jotask.neat.jneat.fitness;

import com.badlogic.gdx.math.Vector2;
import com.github.jotask.neat.jneat.NeatEnemy;

/**
 * BasicFitness
 *
 * @author Jose Vives Iznardo
 * @since 10/03/2017
 */
public class BasicFitness implements Fitness {

    private int ticks;

    public BasicFitness() {
        this.reset();
    }

    @Override
    public void update() {
        this.ticks++;
    }

    @Override
    public double evaluate(NeatEnemy e) {
        return ticks - e.getScore() * 1.5;
    }

    @Override
    public void reset() {
        this.ticks = 0;
    }

    public static void main(String[] args) {
        Vector2 a = new Vector2(0,0);
        Vector2 b = new Vector2(10, 10);
        double pa = Math.pow( (a.x - b.x) , 2);
        double pb = Math.pow( (a.y - b.y) , 2);
        double dst = Math.sqrt( pa+ pb);
        System.out.println(dst);
        System.out.println(a.dst(b));
    }

}
