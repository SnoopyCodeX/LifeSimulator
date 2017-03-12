package com.github.jotask.neat.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Camera
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Camera extends OrthographicCamera{

    public Viewport viewport;

    public Camera() {
//        this.viewport = new FitViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT, this);
        float w = 60f;
        float h = 50f;
        this.viewport = new FitViewport(w, h, this);
        this.position.y = -8f;
        this.viewport.apply();
        this.update();
    }

}
