package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Renderer
 *
 * @author Jose Vives Iznardo
 * @since 03/03/2017
 */
public interface Renderer {

    void render(final SpriteBatch sb);

    void debug(final ShapeRenderer sr);

}
