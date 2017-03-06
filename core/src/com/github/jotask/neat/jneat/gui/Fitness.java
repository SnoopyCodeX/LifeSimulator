package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.github.jotask.neat.engine.Util;

import java.util.LinkedList;

/**
 * Fitness
 *
 * @author Jose Vives Iznardo
 * @since 03/03/2017
 */
public class Fitness implements Renderer{

    private int SHOW = 100;

    private LinkedList<Entry> lists;

    private final Rectangle r;

    private double maxFitness, minFitness;

    final float INCR;

    public Fitness(final Gui gui) {
        this.lists = new LinkedList<Entry>();

        this.maxFitness = 0.0;
        this.minFitness = 0.0;

        final OrthographicCamera cam = gui.camera;

        final float offset = 10f;

        float w = (cam.viewportWidth * .5f) - (offset * 2);
        float h = 100f;

        float x = (cam.position.x - ( cam.viewportWidth * .5f )) + offset;
        float y = cam.position.y - (cam.viewportHeight * .5f) + offset;

        this.r = new Rectangle(x, y, w, h);

        INCR = .49f;

    }

    public void addFitness(final int generation, final double fitness) {
        if(this.lists.size() >= SHOW){
            this.lists.removeFirst();
        }
        final Entry e = new Entry(generation, fitness);
        this.lists.addLast(e);

        if(minFitness > fitness){
            minFitness = fitness;
        }

        if(maxFitness < fitness){
            maxFitness = fitness;
        }

    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void debug(ShapeRenderer sr) {

        // Draw background
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.NAVY);
        sr.getColor().a = .5f;
        sr.rect(r.x, r.y, r.width, r.height);

        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.LIME);
        sr.line(r.x, r.y, r.x + r.width, r.y);
        sr.line(r.x, r.y, r.x, r.y + r.height);

        sr.setColor(Color.BLACK);

        final int size = (lists.size() > SHOW)? SHOW: lists.size();

        float x = 1;

        for(int i = 1; i < size; i++){

            Entry last = lists.get(i - 1);
            Entry curr = lists.get(i);

            float x1 = (float) Util.map(x - INCR, 0d, 100, r.x, r.width);
            float y1 = (float) Util.map(last.fitness, minFitness, maxFitness, r.y, (r.y + r.height));

            float x2 = (float) Util.map(x, 0d, 100, r.x, r.width);
            float y2 = (float) Util.map(curr.fitness, minFitness, maxFitness, r.y, (r.y + r.height));

            sr.line(x1, y1, x2, y2);

            x += INCR;

        }
    }

    private static class Entry{
        public final int generation;
        public final double fitness;
        private Entry(int generation, double fitness) {
            this.generation = generation;
            this.fitness = fitness;
        }
    }

}
