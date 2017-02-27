package com.github.jotask.neat.engine;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * CollisionFilter
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
public class CollisionFilter {

    public enum EENTITY { WALLS, PLAYER, ENEMY, PLAYER_FRIEND, ENEMY_FRIEND, PLAYER_RADAR, ENEMY_RADAR }

    private static class ENTITY {

        public static final short WALLS = 1;
        public static final short PLAYER = 2;
        public static final short ENEMY = 4;
        public static final short PLAYER_FRIEND = 8;
        public static final short ENEMY_FRIEND = 16;
        public static final short PLAYER_RADAR = 32;
        public static final short ENEMY_RADAR = 64;

    }

    private static class MASK {

//        public static final short WALLS = -1;
//        public static final short PLAYER = ENTITY.WALLS | ENTITY.ENEMY | ENTITY.ENEMY_FRIEND | ENTITY.ENEMY_RADAR;
//        public static final short ENEMY = ENTITY.WALLS | ENTITY.PLAYER | ENTITY.PLAYER_FRIEND | ENTITY.PLAYER_RADAR;
//        public static final short PLAYER_FRIEND = ~ENTITY.PLAYER;
//        public static final short ENEMY_FRIEND = ~ENTITY.ENEMY;
//        public static final short PLAYER_RADAR = ENTITY.ENEMY;
//        public static final short ENEMY_RADAR = ENTITY.PLAYER;

        public static final short WALLS = -1;
        public static final short PLAYER = ~ENTITY.PLAYER;
        public static final short ENEMY = ~ENTITY.ENEMY;
        public static final short PLAYER_FRIEND = ~ENTITY.PLAYER;
        public static final short ENEMY_FRIEND = ~ENTITY.ENEMY;
        public static final short PLAYER_RADAR = ENTITY.ENEMY;
        public static final short ENEMY_RADAR = ENTITY.PLAYER;

    }

    public static void setMask(FixtureDef fix, EENTITY entity){

        switch (entity) {
            case WALLS:
                set(fix, ENTITY.WALLS, MASK.WALLS);
                break;
            case PLAYER:
                set(fix, ENTITY.PLAYER, MASK.PLAYER);
                break;
            case ENEMY:
                set(fix, ENTITY.ENEMY, MASK.ENEMY);
                break;
            case PLAYER_FRIEND:
                set(fix, ENTITY.PLAYER_FRIEND, MASK.PLAYER_FRIEND);
                break;
            case ENEMY_FRIEND:
                set(fix, ENTITY.ENEMY_FRIEND, MASK.ENEMY_FRIEND);
                break;
            case PLAYER_RADAR:
                set(fix, ENTITY.PLAYER_RADAR, MASK.PLAYER_RADAR);
                break;
            case ENEMY_RADAR:
                set(fix, ENTITY.ENEMY_RADAR, MASK.ENEMY_RADAR);
                break;
        }

    }

    private static void set(FixtureDef fd, short category, short mask){
        fd.filter.categoryBits = category;
        fd.filter.maskBits = mask;
    }

    public static void setMask(Fixture fix, EENTITY entity){

        switch (entity) {
            case WALLS:
                set(fix, ENTITY.WALLS, MASK.WALLS);
                break;
            case PLAYER:
                set(fix, ENTITY.PLAYER, MASK.PLAYER);
                break;
            case ENEMY:
                set(fix, ENTITY.ENEMY, MASK.ENEMY);
                break;
            case PLAYER_FRIEND:
                set(fix, ENTITY.PLAYER_FRIEND, MASK.PLAYER_FRIEND);
                break;
            case ENEMY_FRIEND:
                set(fix, ENTITY.ENEMY_FRIEND, MASK.ENEMY_FRIEND);
                break;
            case PLAYER_RADAR:
                set(fix, ENTITY.PLAYER_RADAR, MASK.PLAYER_RADAR);
                break;
            case ENEMY_RADAR:
                set(fix, ENTITY.ENEMY_RADAR, MASK.ENEMY_RADAR);
                break;
        }

    }

    private static void set(Fixture fd, short category, short mask){
        Filter f = fd.getFilterData();
        f.categoryBits = category;
        f.maskBits = mask;
        fd.setUserData(f);
    }

}
