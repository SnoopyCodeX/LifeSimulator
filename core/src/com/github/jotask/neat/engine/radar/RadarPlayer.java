package com.github.jotask.neat.engine.radar;

import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.engine.entity.Player;

import java.util.LinkedList;

/**
 * RadarPlayer
 *
 * @author Jose Vives Iznardo
 * @since 24/02/2017
 */
public class RadarPlayer extends Radar<Player>{

    public final LinkedList<Enemy> enemies;

    public RadarPlayer() {
        this.enemies = new LinkedList<Enemy>();
    }

    public void inRange(final Enemy e){ this.enemies.add(e); }

    public void outRange(final Enemy e){ this.enemies.remove(e); }

    public LinkedList<Enemy> getFoods() { return enemies; }

    public Enemy getClosest(){
        Enemy closest = null;
        float dst = Float.MAX_VALUE;
        for(Enemy e: enemies){
            float d = this.getEnt().getBody().getPosition().dst(e.getBody().getPosition());
            if(d < dst){
                closest = e;
                dst = d;
            }
        }
        return closest;
    }
}
