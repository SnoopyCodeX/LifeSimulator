package com.github.jotask.neat.jneat.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.jneat.Jota;
import com.github.jotask.neat.jneat.NeatEnemy;
import com.github.jotask.neat.jneat.genetics.Synapse;
import com.github.jotask.neat.jneat.gui.Renderer;
import com.github.jotask.neat.jneat.util.Ref;
import com.github.jotask.neat.jneat.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * NetworkRenderer
 *
 * @author Jose Vives Iznardo
 * @since 05/03/2017
 */
public class NetworkRenderer implements Renderer {

    final Jota neat;

    private final Rectangle rectangle;

    private Network network;

    final Color c = Color.WHITE;

    final Map<Integer, Cell> graph;

    public NetworkRenderer(final Jota neat) {
        this.neat = neat;
        final OrthographicCamera cam = Neat.get().getGui().getCamera();
        float w = cam.viewportWidth * .5f;
        float h = Cell.SIZE * Ref.INPUTS;
//        float x = cam.position.x - (w * .5f);
        float x = cam.position.x - 25;
        float y = cam.position.y - (h *.5f);

//        w = cam.viewportWidth * .5f;
//        h = cam.viewportHeight * .5f;
//
//        x = cam.position.x - ( w * .5f);
//        y = cam.position.y  - (h * .5f);

        y -= 150f;
        x += 20f;

        this.rectangle = new Rectangle(x, y, w, h);

        graph = new HashMap<Integer, Cell>();

        this.c.a = .5f;

    }

    public void createNetwork(NeatEnemy e) {
        this.graph.clear();

        if(e.getGenome().getNetwork() == this.network){
            return;
        }else{
            this.network = e.getGenome().getNetwork();
        }

        // Create new network for render
        float yInpStart = rectangle.y + (rectangle.height * .5f) + ((Cell.SIZE * Ref.INPUTS) * .5f) - Cell.SIZE;
        float yOutStart = rectangle.y + (rectangle.height * .5f) + ((Cell.SIZE * Ref.OUTPUTS) * .5f) - Cell.SIZE;

        int input = 0;
        int output = 0;

        float minX = rectangle.x + Cell.SIZE;
        float maxX = rectangle.x + rectangle.width - Cell.SIZE;

        for (final Map.Entry<Integer, Neuron> entry : this.network.network.entrySet()) {
            final int i = entry.getKey();
            final Neuron neuron = entry.getValue();

            final float x;
            final float y;

            if (Util.isInput(i)) {

                x = rectangle.x;
                y = yInpStart - (Cell.SIZE * input++);

                graph.put(i, new Cell(x, y, neuron));

            } else if (Util.isOutput(i)) {

                x = rectangle.x + rectangle.width - Cell.SIZE;
                y = yOutStart - (Cell.SIZE * output++);

                graph.put(i, new Cell(x, y, neuron));

            } else {

//                x = rectangle.x + (rectangle.width * .5f);
                x = (minX + maxX) * .5f;
                y = rectangle.y + (rectangle.height * .5f);
                graph.put(i, new Cell(x, y, neuron, 1f, Cell.SIZE * .5f));

            }
        }


        final float a = .75f;
        final float b = .25f;

        for (int n = 0; n < 4; n++) {
            for (final Synapse gene : e.getGenome().getGenes()) {
                if (gene.isEnabled()) {
                    final Cell c1 = graph.get(gene.getInput());
                    final Cell c2 = graph.get(gene.getOutput());
                    if (Util.isHidden(gene.getInput())) {
                        c1.x = (a * c1.x + b * c2.x);
                        if (c1.x >= c2.x)
                            c1.x = c1.x - Cell.SIZE;
                        if (c1.x < minX) c1.x = minX;
                        if (c1.x > maxX) c1.x = maxX;
                        c1.y = (a * c1.y + b * c2.y);
                    }
                    if (Util.isHidden(gene.getOutput())) {
                        c2.x = (b * c1.x + a * c2.x);
                        if (c1.x >= c2.x)
                            c2.x = c2.x + Cell.SIZE;
                        if (c2.x < minX) c2.x = minX;
                        if (c2.x > maxX) c2.x = maxX;
                        c2.y = (b * c1.y + a * c2.y);
                    }
                }
            }
        }

    }

    @Override
    public void render(SpriteBatch sb) { }

    @Override
    public void debug(final ShapeRenderer sr) {
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(c);
        sr.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        if(graph == null && !graph.isEmpty())
            return;

        for (final Cell cell : graph.values()) {
            cell.debug(sr);
        }

        if(this.neat.getBest() == null){
            return;
        }

        for (final Synapse gene : this.neat.getBest().getGenome().getGenes()) {
            if (gene.isEnabled()) {
                final Cell c1 = graph.get(gene.getInput());
                final Cell c2 = graph.get(gene.getOutput());

                if(c1 == null || c2 == null){
                    continue;
                }

                final Color color;
                if (Neuron.sigmoid(gene.getWeight()) > 0.0) {
                    color = Color.GREEN;
                }else {
                    color = Color.RED;
                }
                sr.setColor(color);
                final float s1 = c1.size * .5f;
                final float s2 = c2.size * .5f;
                sr.line(c1.x + s1, c1.y + s1, c2.x + s2, c2.y + s2);
            }
        }
    }

}
