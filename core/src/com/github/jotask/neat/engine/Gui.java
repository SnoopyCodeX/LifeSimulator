package com.github.jotask.neat.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.jotask.neat.Neat;

/**
 * Gui
 *
 * @author Jose Vives Iznardo
 * @since 12/02/2017
 */
public class Gui {

    public static final float SPACE = 15f;
    public static final float OFFSET = 10f;

    private final Neat neat;

    private final OrthographicCamera camera;

    private final BitmapFont font;

    public Gui(Neat neat) {
        this.neat = neat;
        this.font = neat.getFont();
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render(SpriteBatch sb){
        sb.begin();
        float x = -camera.viewportWidth / 2f;
        float y = camera.viewportHeight / 2f;
        x += OFFSET;
        y -= OFFSET;
        int i = 0;
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, y - SPACE * i++);
        font.draw(sb, "JavaHeap: " + Util.bytesToMb(Gdx.app.getJavaHeap()), x, y - SPACE * i++);
        font.draw(sb, "NativeHeap: " + Util.bytesToMb(Gdx.app.getNativeHeap()), x, y - SPACE * i++);
//        font.draw(sb, "Enemies: " + neat.getNeat().getAlive() + " / " + Constants.POPULATION, x, y - SPACE * i++);
//        font.draw(sb, "Generation: " + neat.getNeat().getGeneration(), x, y - SPACE * i++) ;
//        font.draw(sb, "Fitness: " + neat.getNeat().getMaxFitness(), x, y - SPACE * i++) ;
//        font.draw(sb, "Species: " + neat.getNeat().pool.species.size(), x, y - SPACE * i) ;

//        {
//            final float xx = (camera.viewportWidth / 2f) - 50;
//            font.draw(sb, "DOWN", xx - 3f, 70);
//            font.draw(sb, "UP", xx + 20, 30);
//            font.draw(sb, "RIGHT", xx, -20);
//            font.draw(sb, "LEFT", xx + 7f, -60);
//        }

        sb.end();
    }

    public OrthographicCamera getCamera() { return camera; }

}
