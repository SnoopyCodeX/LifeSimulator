package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.jneat.Jota;

/**
 * Gui
 *
 * @author Jose Vives Iznardo
 * @since 03/03/2017
 */
public class Gui implements Renderer {

    final Jota jNeat;
    final OrthographicCamera camera;
    final BitmapFont font;

    private final Information information;
    private final Fitness fitness;

    public Gui(final Jota jNeat) {
        this.jNeat = jNeat;
        final Neat neat = Neat.get();
        this.font = neat.getFont();
        this.camera = neat.getGui().getCamera();

        this.information = new Information(jNeat);
        this.fitness = new Fitness(this);

    }

    @Override
    public void render(final SpriteBatch sb) {
        this.information.render(sb);
        this.fitness.render(sb);
    }

    @Override
    public void debug(final ShapeRenderer sr) {
        this.information.debug(sr);
        this.fitness.debug(sr);
    }

    public Fitness getFitness() { return fitness; }

}