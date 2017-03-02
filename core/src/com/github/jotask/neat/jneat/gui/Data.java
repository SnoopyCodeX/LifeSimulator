package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.github.jotask.neat.engine.Util;

import java.util.LinkedList;

/**
 * Data
 *
 * @author Jose Vives Iznardo
 * @since 28/02/2017
 */
public class Data {

    final OrthographicCamera camera;

    private final int TODISPLAY;

    final float INCR;

    private static class Fitness{
        final int generation;
        final double fitness;
        public Fitness(int generation, double fitness) {
            this.generation = generation;
            this.fitness = fitness;
        }
    }

    private final Rectangle r;

    public LinkedList<Fitness> fitness;

    private double maxFitness, minFitness;

    public Data(final OrthographicCamera cam) {
        this.camera = cam;
        this.fitness = new LinkedList<Fitness>();

        float w = cam.viewportWidth - 3f;
        float h = 10;

        float x = (cam.position.x -w/ 2f);
        float y = cam.position.y - (cam.viewportHeight / 2f) + .5f;

        this.r = new Rectangle(x, y, w, h);

        maxFitness = 0;
        minFitness = 0;

        TODISPLAY = 65;

        INCR = 1f;


    }

    public void addFitness(final int generation, final double fff){
        Fitness f = new Fitness(generation, fff);
        fitness.addLast(f);

        if(fff > maxFitness)
            maxFitness = fff;

        if(fff < minFitness)
            minFitness = fff;

        if(fitness.size() > TODISPLAY) {
            fitness.removeFirst();
        }

    }

    public void render(final SpriteBatch sb, final ShapeRenderer sr){

        // Draw background
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.NAVY);
        sr.getColor().a = .5f;
        sr.rect(r.x, r.y, r.width, r.height);

        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.LIME);
        sr.line(r.x, r.y, r.x + r.width, r.y);
        sr.line(r.x, r.y, r.x, r.y + r.height);

        if(fitness.isEmpty())
            return;

        sr.setColor(Color.BLACK);

        final int size = (fitness.size() > TODISPLAY)? TODISPLAY: fitness.size();

        float x = 1;

        for(int i = 1; i < size; i++){

            if(fitness.size() <= 2)
                break;

            Fitness last = fitness.get( i - 1);
            Fitness curr = fitness.get(i);

            float x1 = (float) Util.map(x - INCR, 0d, 100, r.x, r.width);
            float y1 = (float) Util.map(last.fitness, minFitness, maxFitness, r.y, (r.y + r.height));

            float x2 = (float) Util.map(x, 0d, 100, r.x, r.width);
            float y2 = (float) Util.map(curr.fitness, minFitness, maxFitness, r.y, (r.y + r.height));

            sr.line(x1, y1, x2, y2);

            x += INCR;

        }

    }

}
