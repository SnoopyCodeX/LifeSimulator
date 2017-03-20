package com.github.jotask.neat.engine;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.jotask.neat.jneat.util.JException;

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

    }

    private static class MASK {

        public static final short WALLS = -1;
        public static final short PLAYER = CATEGORY.WALLS | CATEGORY.ENEMY_FRIEND;
        public static final short ENEMY = CATEGORY.WALLS | CATEGORY.PLAYER_FRIEND;
        public static final short PLAYER_FRIEND = CATEGORY.WALLS | CATEGORY.ENEMY;
        public static final short ENEMY_FRIEND = CATEGORY.WALLS | CATEGORY.PLAYER;

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
            default:
                throw new JException("Collision filter not supported");
        }

    }

    private static void set(Fixture fd, short category, short mask){
        Filter f = fd.getFilterData();
        f.categoryBits = category;
        f.maskBits = mask;
        fd.setFilterData(f);
    }

}
