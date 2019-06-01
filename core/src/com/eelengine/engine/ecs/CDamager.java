package com.eelengine.engine.ecs;

import com.artemis.Component;

/**
 * A null class to copy.
 */
public class CDamager extends Component {
    public static short MYSTICAL=1;
    public int amount=0;
    public int team=0;
    public short type=0;

    /**
     * Sets {@link #amount}
     */
    public CDamager set(int damage){
        this.amount =damage;
        return this;
    }
    /**
     * Sets {@link #amount} and {@link #team}
     */
    public CDamager set(int damage, int team) {
        this.amount = damage;
        this.team = team;
        return this;
    }
    /**
     * Sets {@link #amount} and {@link #team}, and adds input types
     * @param types types to add to {@link #type}
     */
    public CDamager set(int damage, int team, short ... types){
        this.amount =damage;
        this.team=team;
        for(short t:types)addType(t);
        return this;
    }

    /**
     * Adds a type to the bitfield {@link #type}
     */
    public CDamager addType(short type){
        this.type|=type;
        return this;
    }

    /**
     * Removes a type from the bitfield {@link #type}
     */
    public CDamager removeType(short type){
        this.type&=~type;
        return this;
    }

    /**
     * Checks the {@link #type} bitfield for @param type
     * @param type type to check
     * @return true if bullet is of type @param type
     */
    public boolean isType(short type){
        return (this.type&type)!=0;
    }

//    public DamageEvent getEvent(){
//        return new DamageEvent(amount,type,);
//    }
}
