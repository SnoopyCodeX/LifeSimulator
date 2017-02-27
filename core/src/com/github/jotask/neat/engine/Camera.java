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
        this.viewport = new FitViewport(21f, 11f, this);
//        this.viewport = new FitViewport(31f, 21f, this);
        this.viewport.apply();
    }

}
