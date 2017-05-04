package com.github.jotask.neat.engine.controller;

import com.github.jotask.neat.engine.weapon.Weapon;

/**
 * WeaponController
 *
 * @author Jose Vives Iznardo
 * @since 20/03/2017
 */
public class WeaponController {

    private final Weapon weapon;

    public WeaponController(final Weapon weapon) { this.weapon = weapon; }

    public void left(){ weapon.shotDirection.set(-1, 0); }

    public void right(){ weapon.shotDirection.set(1, 0); }

    public void up(){ weapon.shotDirection.set(0, -1); }

    public void down(){ weapon.shotDirection.set(0, 1); }

}
