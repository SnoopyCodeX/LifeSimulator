package com.github.jotask.neat.jneat.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.jneat.gui.Renderer;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.jneat.util.Util;

/**
 * Cell
 *
 * @author Jose Vives Iznardo
 * @since 19/03/2017
 */
public class Cell implements Renderer{

    static float SIZE = 16;
    static float offset = 3f;

    private final Neuron neuron;

    protected float x;
    protected float y;
    protected final float size;

    private final Color bg;

    private final float alpha;

    Cell(final float x, final float y, final Neuron neuron){
        this(x, y, neuron, .5f, Cell.SIZE);
    }

    Cell(final float x, final float y, final Neuron neuron, final float alpha, final float size) {
        this.x = x;
        this.y = y;
        this.neuron = neuron;
        this.alpha = alpha;
        this.size = size;

        if(Util.isInput(this.neuron.getId())) {
            if(this.neuron.getId() == Ref.Inputs.bias.ordinal()) {
                bg = Color.CLEAR;
            }else {
                bg = Color.LIME;
            }
        }else if(Util.isOutput(this.neuron.getId())){
            bg = Color.BROWN;
        }else{
            bg = Color.BLACK;
        }
    }

    @Override
    public void render(SpriteBatch sb) { }

    @Override
    public void debug(final ShapeRenderer sr) {
        Color color;
        if (this.neuron.getValue() > 0.0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }

        color.a = alpha;

        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(bg);
        sr.rect(x, y, size, size);

        sr.setColor(color);
        sr.rect(x + offset, y + offset, (size - (offset * 2)), size - (offset * 2));

        sr.setColor(Color.BLACK);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(x, y, size, size);

    }

}
