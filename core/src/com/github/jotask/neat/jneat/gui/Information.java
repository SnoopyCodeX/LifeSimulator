package com.github.jotask.neat.jneat.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.jneat.Jota;
import com.github.jotask.neat.jneat.util.Util;

import java.util.LinkedList;

import static com.github.jotask.neat.engine.Gui.OFFSET;
import static com.github.jotask.neat.engine.Gui.SPACE;

/**
 * Information
 *
 * @author Jose Vives Iznardo
 * @since 03/03/2017
 */
public class Information implements Renderer {

    static abstract class Inform<T>{
        final BitmapFont font;
        final float x;
        final float y;
        final GlyphLayout glyph;
        final String text;
        String tmp;
        T t;

        public Inform(LinkedList<Inform> info, final BitmapFont font, float x, float y, String text) {
            info.add(this);
            this.font = font;
            this.x = x;
            this.y = y;
            this.text = text;
            this.glyph = new GlyphLayout();
        }

        abstract void set();

        void render(final SpriteBatch sb){
            font.draw(sb, tmp, x - glyph.width, y);
        }

        void update(){
            this.tmp = this.text + ": " + t.toString();
            this.glyph.setText(font, tmp);
        }
    }

    private LinkedList<Inform> info;

    public Information(final Jota neat) {
        this.info = new LinkedList<Inform>();

        OrthographicCamera c = Neat.get().getGui().getCamera();

        float x = c.position.x + (c.viewportWidth * .5f);
        float y = c.position.y + (c.viewportHeight * .5f);
        x -= OFFSET;
        y -= OFFSET;
        int i = 0;

        final BitmapFont font = Neat.get().getFont();

        new Inform<Integer>(this.info, font, x, y - SPACE * i++, "Generation") {
            @Override
            void set() { this.t = neat.getPop().generation; }
        };

        new Inform<Integer>(this.info, font, x, y - SPACE * i++, "Species") {
            @Override
            void set() { this.t = neat.getPopulation(); }
        };

        new Inform<Integer>(this.info, font, x, y - SPACE * i++, "Active") {
            @Override
            void set() { this.t = neat.getManager().manyActived(); }
        };

        new Inform<Integer>(this.info, font, x, y - SPACE * i++, "Disabled") {
            @Override
            void set() { this.t = neat.getManager().manyDisabled(); }
        };

        new Inform<Double>(this.info, font, x, y - SPACE * i++, "MaxFitness" ) {
            @Override
            void set() { this.t = Util.limitDecimals(neat.getPop().maxFitness, 2); }
        };

    }

    @Override
    public void render(SpriteBatch sb) {
        for(final Inform i: info){
            i.set();
            i.update();
            i.render(sb);
        }
    }

    @Override
    public void debug(ShapeRenderer sr) { }

}
