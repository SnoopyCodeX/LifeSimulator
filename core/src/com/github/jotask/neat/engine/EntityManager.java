package com.github.jotask.neat.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.entity.Enemy;
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

    public static boolean add(final Entity entity){
        if(instance == null)
            get();

        if(entity == null)
            return false;

        if(entity instanceof Food){
            instance.foods++;
        }else if(entity instanceof Enemy){
            instance.enemies++;
        }

        return instance.entities.add(entity);
    }

    private LinkedList<Entity> entities;

    private int foods = 0;
    private int enemies = 0;

    private EntityManager() { this.entities = new LinkedList<Entity>(); }

    public void update() {

        final LinkedList<Entity> newPopulation = new LinkedList<Entity>(entities);
        int f = 0;

        for(Entity e: entities){

            e.input();
            e.update();

            if(e.isDie()) {
                e.die();
                newPopulation.remove(e);
                Neat.get().getWorld().destroyBody(e.getBody());

                if(e instanceof Food){
                    instance.foods--;
                    f++;
                }else if(e instanceof Enemy){
                    instance.enemies--;
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

    public LinkedList<Entity> getEntities() { return entities; }

    public int getFoods() { return foods; }
    public int getEnemies() { return enemies; }

}
