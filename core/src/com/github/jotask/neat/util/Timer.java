package com.github.jotask.neat.util;

/**
 * Timer
 *
 * @author Jose Vives Iznardo
 * @since 12/02/2017
 */
public class Timer {

    private long time;
    private long next;

    public Timer(float sec){
        setTime(sec);
    }

    public static long toSec(float seg){

        Float f = seg;

        f *= 1000000000;

        return f.longValue();

    }

    public void reset(){
        this.next = System.nanoTime() + time;
    }

    public boolean isPassed(){ return this.isPassed(false);}
    public boolean isPassed(boolean reset){

        boolean b = (System.nanoTime() > this.next);

        if(reset && b)
            reset();

        return b;

    }

    public void add(float add){
        long n = Timer.toSec(add);
        time += n;
        this.reset();
    }

    public void setTime(float sec){
        time = Timer.toSec(sec);
        reset();
    }

    public void increase(final float sec){
        float next = Timer.toSec(sec);
        this.time += next;
        this.reset();
    }

}