package com.eelengine.engine.ecs;

import com.artemis.Component;

import java.util.LinkedList;

/**
 * A null class to copy.
 */
public class CDamageable extends Component {
    int health=1;
    int maxHealth=1;
    LinkedList<DamageEvent> damageEvents;

    public CDamageable() {
        damageEvents=new LinkedList<>();
    }

    /**
     * Initializes {@link #health} and {@link #maxHealth}
     */
    public CDamageable set(int health, int maxHealth){
        this.health=health;
        this.maxHealth=maxHealth;
        return this;
    }

    /**
     * Initializes {@link #maxHealth} and sets {@link #health} to full.
     * @param maxHealth the health to initialize {@link #health} and {@link #maxHealth} to.
     */
    public CDamageable set(int maxHealth){
        this.health=maxHealth;
        this.maxHealth=maxHealth;
        return this;
    }

    public CDamageable addDamage(int amt, int source){
        damageEvents.add(new DamageEvent(amt,0,source));
        return this;
    }
    public CDamageable addDamage(int amt, int type, int source){
        damageEvents.add(new DamageEvent(amt,type,source));
        return this;
    }
    public CDamageable addDamage(DamageEvent event){
        damageEvents.add(event);
        return this;
    }
}
