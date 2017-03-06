package com.github.jotask.neat.jneat;

import com.github.jotask.neat.Neat;

import java.util.LinkedList;

/**
 * EntityManager
 *
 * @author Jose Vives Iznardo
 * @since 03/03/2017
 */
public final class EnemyManager {

    private LinkedList<NeatEnemy> active;
    private LinkedList<NeatEnemy> disabled;

    public EnemyManager() {
        this.active = new LinkedList<NeatEnemy>();
        this.disabled = new LinkedList<NeatEnemy>();

        for(int i = 0; i < Constants.POPULATION; i++){
            final NeatEnemy e = Neat.get().getFactory().getNeatEnemy();
            e.disable();
            this.disabled.add(e);
        }

    }

    public void clear(){
        for(final NeatEnemy n: active){
            n.disable();
            this.disabled.add(n);
        }
        this.active.clear();
    }

    public LinkedList<NeatEnemy> getActive() { return active; }

    public void spawn(final Species species, final Genome genome){

        final NeatEnemy tmp = this.disabled.pollFirst();
        tmp.activate(species, genome);
        this.active.add(tmp);
    }

    public void dispose(){
        clear();
        for(final NeatEnemy e: disabled){
            e.kill();
        }
    }

}
