package com.github.jotask.neat.engine.controller;

import com.github.jotask.neat.engine.JRandom;
import com.github.jotask.neat.engine.entity.Enemy;

/**
 * PlayerController
 *
 * @author Jose Vives Iznardo
 * @since 11/02/2017
 */
public class EnemyController {

    private final Enemy enemy;

    public EnemyController(Enemy enemy) {
        this.enemy = enemy;
    }

    public void moveRandom(){ enemy.velocity.set(JRandom.getDir()); }

    public void left(){ enemy.velocity.set(-1, 0); }

    public void right(){ enemy.velocity.set(1, 0); }

    public void up(){ enemy.velocity.set(0, 1); }

    public void down(){ enemy.velocity.set(0, -1); }

}
