package com.github.jotask.neat.jneat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.jneat.neurons.Neuron;

/**
 * Cell
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
class Cell {

    int id;
    float x;
    float y;
    final double value;
    final Neuron.Type type;

    static float SIZE = 1f;

    public Cell(final int i, final float x, final float y, final double value, final Neuron.Type type) {
        this.id = i;
        this.x = x;
        this.y = y;
        this.value = value;
        this.type = type;
    }

    public void render(final SpriteBatch sb){

    }

    public void debug(final ShapeRenderer sr){
        final double value = this.value;
        float s = SIZE;

        float xx = this.x;
        float yy = this.y;

        switch (type){
            case INPUT:
//                    sr.setColor(Color.YELLOW);
                break;
            case HIDDEN:
                s /= 2f;
                xx += s * .5f;
                yy += s * .5f;
                break;
            case OUTPUT:
//                    sr.setColor(Color.CYAN);
                break;
            default:
                sr.setColor(Color.WHITE);
        }

        if(value > 0.0){
            sr.setColor(Color.GREEN);
        }else{
            sr.setColor(Color.RED);
        }

        sr.getColor().a = .25f;

        sr.set(ShapeRenderer.ShapeType.Filled);

        sr.rect(xx, yy, s, s);

        sr.setColor(Color.BLACK);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.rect(xx, yy, s, s);

    }

}
