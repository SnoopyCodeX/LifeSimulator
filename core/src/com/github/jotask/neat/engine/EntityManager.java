package com.github.jotask.neat.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.entity.Entity;
import com.github.jotask.neat.engine.entity.Food;

import java.util.LinkedList;

/**
 * EntityManager
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class EntityManager{

    private static EntityManager instance;
    public static final EntityManager get(){
        if(instance == null)
            instance = new EntityManager();
        return instance;
    }

    public static boolean add(final Food entity){
        if(instance == null)
            get();

        if(entity == null)
            return false;

        return instance.entities.add(entity);
    }

    private LinkedList<Food> entities;

    private EntityManager() {
        this.entities = new LinkedList<Food>();
    }

    public void update() {

        final LinkedList<Food> newPopulation = new LinkedList<Food>(entities);
        int f = 0;

        for(Entity e: entities){

            e.input();
            e.update();

            if(e.isDie()) {
                e.die();
                newPopulation.remove(e);
                Neat.get().getWorld().destroyBody(e.getBody());

                if(e instanceof Food){
                    f++;
                }

            }

        }
        entities = newPopulation;
        for(int i = 0; i < f; i++){
            Neat.get().getFactory().food();
        }
    }

    public void render(SpriteBatch sb) {
        for(Entity e: entities)
            e.render(sb);
    }

    public void debug(ShapeRenderer sr) {
        for(Entity e: entities)
            e.debug(sr);
    }

    public void dispose(){
        EntityManager.instance = null;
    }

    public LinkedList<Food> getEntities() { return entities; }

}
