package com.github.jotask.neat.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.jneat.Population;

/**
 * Gui
 *
 * @author Jose Vives Iznardo
 * @since 12/02/2017
 */
public class Gui {

    private final Neat neat;
    private final OrthographicCamera camera;

    private final BitmapFont font;

    public Gui(Neat neat) {
        this.neat = neat;
        this.font = neat.getFont();
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render(SpriteBatch sb){
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        float x = -camera.viewportWidth / 2f;
        float y = camera.viewportHeight / 2f;
        float space = 15f;
        float off = 10f;
        x += off;
        y -= off;
        int i = 0;
        font.draw(sb, "Enemies: " + neat.getNeat().getAlive() + " / " + Population.POPULATION, x, y - space * i++);
        font.draw(sb, "Food: " + EntityManager.get().getFoods(), x, y - space * i++) ;
        font.draw(sb, "Generation: " + neat.getNeat().getGeneration(), x, y - space * i++) ;

        {
            final float xx = (camera.viewportWidth / 2f) - 50;
            font.draw(sb, "DOWN", xx - 3f, 70);
            font.draw(sb, "UP", xx + 20, 30);
            font.draw(sb, "RIGHT", xx, -20);
            font.draw(sb, "LEFT", xx + 7f, -60);
        }

        sb.end();
    }

}
