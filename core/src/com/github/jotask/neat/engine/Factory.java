package com.github.jotask.neat.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.jotask.neat.Neat;
import com.github.jotask.neat.engine.entity.Entity;
import com.github.jotask.neat.engine.entity.Player;
import com.github.jotask.neat.jneat.NeatEnemy;
import com.github.jotask.neat.util.JRandom;

/**
 * Factory
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class Factory {

    private static Factory instance;

    public static final Factory get(){
        return instance;
    }

    private Neat neat;

    public Factory(final Neat neat) {
        this.neat = neat;
        Factory.instance = this;
    }

    public final Player getPlayer(){

        final float radius = .5f;

        final Vector2 p = JRandom.randomPositionPlayer();
        final Body body = createBody(p.x, p.y);

        Fixture playerBody = createEntityBody(body, radius);
//        Fixture radarBody = createRadar(body);

//        RadarPlayer radar = new RadarPlayer();
//        radarBody.setUserData(radar);

        Player player = new Player(body);
//        radar.setEnt(player);
        playerBody.setUserData(player);

        body.setUserData(Entity.Type.PLAYER);

//        EntityManager.add(player);

        CollisionFilter.setMask(playerBody, CollisionFilter.ENTITY.PLAYER);
//        CollisionFilter.setMask(radarBody, CollisionFilter.ENTITY.PLAYER_RADAR);

        return player;


    }

    public final NeatEnemy getNeatEnemy(){

        final Vector2 p = JRandom.randomPosition();
        float radius = .5f;

        final Body body = createBody(p.x, p.y);

        Fixture enemyBody = createEntityBody(body, radius);

//        Fixture radarBody = createRadar(body);

//        RadarEnemy radar = new RadarEnemy();
//        radarBody.setUserData(radar);

        NeatEnemy enemy = new NeatEnemy(body);

//        radar.setEnt(enemy);

        body.setUserData(Entity.Type.ENEMY);

        enemyBody.setUserData(enemy);

        EntityManager.add(enemy);

        CollisionFilter.setMask(enemyBody, CollisionFilter.ENTITY.ENEMY);
//        CollisionFilter.setMask(radarBody, CollisionFilter.ENTITY.ENEMY_RADAR);

        return enemy;

    }

//    public Enemy getEnemy(){
//        final Vector2 p = getRandomPositionOnWorld();
//        float x = p.x;
//        float y = p.y;
//        float radius = .5f;
//        return this.getEnemy(x, y, radius);
//    }
//
//    public Enemy getEnemy(final float x, final float y, float radius){
//
//        final Body body = createBody(x, y);
//
//        Fixture enemyBody = createEntityBody(body, radius);
//
//        Fixture radarBody = createRadar(body);
//
//        RadarEnemy radar = new RadarEnemy();
//        radarBody.setUserData(radar);
//
//        Enemy enemy = new Enemy(body, radar);
//
//        radar.setEnt(enemy);
//
//        enemyBody.setUserData(enemy);
//
//        body.setUserData(Entity.Type.ENEMY);
//
//        EntityManager.add(enemy);
//
//        return enemy;
//
//    }
//
//    private Fixture createRadar(final Body body){
//        CircleShape shape = new CircleShape();
//        shape.setRadius(RadarEnemy.SIZE);
//        FixtureDef fd = new FixtureDef();
//        fd.isSensor = true;
//        fd.shape = shape;
//        Fixture f = body.createFixture(fd);
//        shape.dispose();
//        return f;
//    }

    public void createWalls(){
        float WIDTH = 21f / 2f;
        float HEIGHT = 11f / 2f;

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;

        Vector2[] vertices = new Vector2[]{
                new Vector2(-WIDTH, -HEIGHT),
                new Vector2(WIDTH, -HEIGHT),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(-WIDTH, HEIGHT)
        };

        Body body = neat.getWorld().createBody(bd);

        ChainShape shape = new ChainShape();
        shape.createLoop(vertices);
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;

        Fixture f = body.createFixture(fd);

        CollisionFilter.setMask(f, CollisionFilter.ENTITY.WALLS);

    }
//
//    public Food food(){
//        Vector2 p = getRandomPositionOnWorld();
//        float x = p.x;
//        float y = p.y;
//        float radius = .1f;
//        return this.food(x, y, radius);
//    }

//    public Food food(float x, float y, float radius){
//
//        Body body = this.createBody(x, y);
//
//        Fixture fix = this.createEntityBody(body, radius);
//
//        Food food = new Food(body);
//
//        fix.setUserData(food);
//
//        body.setUserData(Entity.Type.FOOD);
//
//        EntityManager.add(food);
//
//        return food;
//
//    }

    private Body createBody(float x, float y){

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(x, y);

        final World world = neat.getWorld();

        Body body = null;
        while(body == null){
            if(world.isLocked()) {
                System.out.println("isLocked");
                continue;
            }
            body = world.createBody(bd);
        }

        return body;

    }

    private Fixture createEntityBody(final Body body, float radius){

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;

        Fixture fix = body.createFixture(fd);

        shape.dispose();

        return fix;

    }

}
