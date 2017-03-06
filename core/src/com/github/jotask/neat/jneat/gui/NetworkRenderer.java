package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.github.jotask.neat.jneat.*;
import com.github.jotask.neat.jneat.neurons.Neuron;

import java.util.HashMap;
import java.util.Map;

/**
 * NetworkRenderer
 *
 * @author Jose Vives Iznardo
 * @since 05/03/2017
 */
public class NetworkRenderer implements Renderer {

    final JNeat neat;
    private final Rectangle rectangle;

    final Color c = Color.CYAN;

    private Network network;

    final Map<Integer, Cell> graph;

    public NetworkRenderer(final JNeat neat, final OrthographicCamera cam) {
        this.neat = neat;
        float w = cam.viewportWidth * .5f;
        float h = Cell.size * Constants.INPUTS;
//        float x = cam.position.x - (w * .5f);
        float x = cam.position.x - 25;
        float y = cam.position.y - (h *.5f);
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

        final float minX = 30;
        final float maxX = rectangle.getWidth() - 42;

        float yOutStart = rectangle.y + (rectangle.height * .5f) - ((Cell.size * Constants.OUTPUTS) * .5f);

        int input = 0;
        int output = 0;

        for (final Map.Entry<Integer, Neuron> entry : this.network.getNetwork().entrySet()) {
            final int i = entry.getKey();
            final Neuron neuron = entry.getValue();
            final float x;
            final float y;

            if (Neuron.isInput(i)) {

                x = rectangle.x;
                y = rectangle.y + Cell.size * input++;
                graph.put(i, new Cell(x, y, neuron));

            } else if (Neuron.isOutput(i)) {

                x = rectangle.x + rectangle.width - Cell.size;
                y = yOutStart + Cell.size * output++;
                graph.put(i, new Cell(x, y, neuron));

            } else {

                x = rectangle.x + (rectangle.width * .5f);
                y = rectangle.y + (rectangle.height * .5f);
                graph.put(i, new Cell(x, y, neuron, 1f));
                throw new RuntimeException("created hidden");

            }
        }

        // TODO Nodes from hidden layers
//        for (int n = 0; n < 4; n++){
//            for (final Synapse gene : e.getGenome().getGenes()){
//                if (gene.isEnabled()) {
//                    final Cell c1 = graph.get(gene.getInput());
//                    final Cell c2 = graph.get(gene.getInput());
//                    if (gene.getInput() >= Constants.INPUTS + Constants.OUTPUTS) {
//                        c1.x = (int) (0.75 * c1.x + 0.25 * c2.x);
//                        if (c1.x >= c2.x)
//                            c1.x = c1.x - 60;
//                        if (c1.x < minX)
//                            c1.x = minX;
//                        if (c1.x > maxX)
//                            c1.x = maxX;
//                        c1.y = (int) (0.75 * c1.y + 0.25 * c2.y);
//                    }
//                    if (gene.getOutput() >= Constants.INPUTS + Constants.OUTPUTS) {
//                        c2.x = (int) (0.25 * c1.x + 0.75 * c2.x);
//                        if (c1.x >= c2.x)
//                            c2.x = c2.x + 60;
//                        if (c2.x < minX)
//                            c2.x = minX;
//                        if (c2.x > maxX)
//                            c2.x = maxX;
//                        c2.y = (int) (0.25 * c1.y + 0.75 * c2.y);
//                    }
//                }
//            }
//        }

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

        for (final Synapse gene : this.neat.getBest().getGenome().getGenes()) {
            if (gene.isEnabled()) {
                final Cell c1 = graph.get(gene.getInput());
                final Cell c2 = graph.get(gene.getOutput());
                final Color color;
                if (Neuron.sigmoid(gene.getWeight()) > 0.0) {
                    color = Color.GREEN;
                }else {
                    color = Color.RED;
                }
                sr.setColor(color);
                final float s = Cell.size * .5f;
                sr.line(c1.x + s, c1.y + s, c2.x + s, c2.y + s);
            }
        }
    }

    static class Cell implements Renderer{

        static float size = 32;
        static float offset = 5f;

        private float x;
        private float y;
        private final Neuron neuron;

        private final Color bg;

        private final float alpha;

        public Cell(final float x, final float y, final Neuron neuron){
            this(x, y, neuron, .5f);
        }

        public Cell(final float x, final float y, final Neuron neuron, final float alpha) {
            this.x = x;
            this.y = y;
            this.neuron = neuron;
            this.alpha = alpha;

            if(Neuron.isInput(neuron.getId())) {
                bg = Color.LIME;
            }else if(Neuron.isOutput(neuron.getId())){
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
            sr.rect(x, y, 32, 32);

            sr.setColor(color);
            sr.rect(x + offset, y + offset, 32 - (offset * 2), 32 - (offset * 2));

            sr.setColor(Color.BLACK);
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.rect(x, y, 32, 32);

        }
    }

}
