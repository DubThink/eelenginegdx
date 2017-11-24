package com.eelengine.engine;

import com.artemis.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A null class to copy.
 */
public class CHealth extends Component {
    int health=1;
    int maxHealth=1;
    Queue<DamageEvent> damageEvents;

    public CHealth() {
        damageEvents=new ConcurrentLinkedDeque<>();
    }

    /**
     * Initializes {@link #health} and {@link #maxHealth}
     */
    public CHealth set(int health, int maxHealth){
        this.health=health;
        this.maxHealth=maxHealth;
        return this;
    }

    /**
     * Initializes {@link #maxHealth} and sets {@link #health} to full.
     * @param maxHealth the health to initialize {@link #health} and {@link #maxHealth} to.
     */
    public CHealth set(int maxHealth){
        this.health=maxHealth;
        this.maxHealth=maxHealth;
        return this;
    }

    public CHealth addDamage(int amt,int source){
        damageEvents.add(new DamageEvent(amt,0,source));
        return this;
    }
    public CHealth addDamage(int amt, int type,int source){
        damageEvents.add(new DamageEvent(amt,type,source));
        return this;
    }
    public CHealth addDamage(DamageEvent event){
        damageEvents.add(event);
        return this;
    }
}
