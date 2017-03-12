package com.github.jotask.neat.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Util
 *
 * @author Jose Vives Iznardo
 * @since 13/02/2017
 */
public class Util {

    private Util(){}

    public static void screenShot(){
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        long n = System.currentTimeMillis();
        String file = "screenshot_" + n + ".png";

        PixmapIO.writePNG(Gdx.files.external(file), pixmap);
        pixmap.dispose();
    }

    /**
     * http://stackoverflow.com/questions/3451553/value-remapping
     *
     * @param value
     * @param low1
     * @param high1
     * @param low2
     * @param high2
     * @return
     */
    public static double map(double value, double low1, double high1, float low2, float high2){
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }

    public static double cutDecimals(final double n, int d){
        double t = 1d;
        for(int i = 0; i < d; i++){
            t *= 10d;
        }
        return Math.round(n * t) / t;
    }

    public static long bytesToMb(long bytes){
        long kb = bytes / 1024;
        long mb = kb / 1024;
        return (mb);
    }

    public static void printMemory(){
        System.out.println("MB: " + bytesToMb(Gdx.app.getJavaHeap()));
    }

}
