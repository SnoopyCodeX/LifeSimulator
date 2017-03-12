package com.github.jotask.neat.engine;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * CollisionFilter
 *
 * @author Jose Vives Iznardo
 * @since 27/02/2017
 */
public class CollisionFilter {

    public enum ENTITY { WALLS, PLAYER, ENEMY, PLAYER_FRIEND, ENEMY_FRIEND, PLAYER_RADAR, ENEMY_RADAR }

    private static class CATEGORY {

        public static final short WALLS = 1;
        public static final short PLAYER = 2;
        public static final short ENEMY = 4;
        public static final short PLAYER_FRIEND = 8;
        public static final short ENEMY_FRIEND = 16;
        public static final short PLAYER_RADAR = 32;
        public static final short ENEMY_RADAR = 64;

    }

    private static class MASK {

        public static final short WALLS = -1;
        public static final short PLAYER = CATEGORY.WALLS;
        public static final short ENEMY = CATEGORY.WALLS;
        public static final short PLAYER_FRIEND = CATEGORY.WALLS;
        public static final short ENEMY_FRIEND = CATEGORY.WALLS;
        public static final short PLAYER_RADAR = CATEGORY.WALLS;
        public static final short ENEMY_RADAR = CATEGORY.WALLS;

    }

    public static void setMask(Fixture fix, ENTITY entity){

        switch (entity) {
            case WALLS:
                set(fix, CATEGORY.WALLS, MASK.WALLS);
                break;
            case PLAYER:
                set(fix, CATEGORY.PLAYER, MASK.PLAYER);
                break;
            case ENEMY:
                set(fix, CATEGORY.ENEMY, MASK.ENEMY);
                break;
            case PLAYER_FRIEND:
                set(fix, CATEGORY.PLAYER_FRIEND, MASK.PLAYER_FRIEND);
                break;
            case ENEMY_FRIEND:
                set(fix, CATEGORY.ENEMY_FRIEND, MASK.ENEMY_FRIEND);
                break;
            case PLAYER_RADAR:
                set(fix, CATEGORY.PLAYER_RADAR, MASK.PLAYER_RADAR);
                break;
            case ENEMY_RADAR:
                set(fix, CATEGORY.ENEMY_RADAR, MASK.ENEMY_RADAR);
                break;
        }

    }

    private static void set(Fixture fd, short category, short mask){
        Filter f = fd.getFilterData();
        f.categoryBits = category;
        f.maskBits = mask;
        fd.setFilterData(f);
    }

}
