package com.github.jotask.neat.engine.entity;

import java.util.LinkedList;

/**
 * Radar
 *
 * @author Jose Vives Iznardo
 * @since 15/02/2017
 */
public class Radar {

    public static final float SIZE = 7f;

    public final LinkedList<Food> foods;

    private Enemy enemy;

    public Radar() {
        this.foods = new LinkedList<Food>();
    }

    public void inRange(final Food food){ this.foods.add(food); }

    public void outRange(final Food food){ this.foods.remove(food); }

    public LinkedList<Food> getFoods() { return foods; }

    public Enemy getEnemy() { return enemy; }

    public void setEnemy(Enemy enemy) {
        if(this.enemy != null) {
            System.err.println("enemy is not null");
            return;
        }
        this.enemy = enemy;
    }

    public Food getClosest(){
        Food closest = null;
        float dst = Float.MAX_VALUE;
        for(Food f: foods){
            float d = enemy.getBody().getPosition().dst(f.getBody().getPosition());
            if(d < dst){
                closest = f;
                dst = d;
            }
        }
        return closest;
    }

}
