package com.github.jotask.neat.engine.radar;

import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.engine.entity.Food;

import java.util.LinkedList;

/**
 * Radar
 *
 * @author Jose Vives Iznardo
 * @since 15/02/2017
 */
public class RadarEnemy extends Radar<Enemy>{

    public final LinkedList<Food> foods;

    public RadarEnemy() {
        this.foods = new LinkedList<Food>();
    }

    public void inRange(final Food food){ this.foods.add(food); }

    public void outRange(final Food food){ this.foods.remove(food); }

    public LinkedList<Food> getFoods() { return foods; }

    public Food getClosest(){
        Food closest = null;
        float dst = Float.MAX_VALUE;
        for(Food f: foods){
            float d = this.getEnt().getBody().getPosition().dst(f.getBody().getPosition());
            if(d < dst){
                closest = f;
                dst = d;
            }
        }
        return closest;
    }

}
