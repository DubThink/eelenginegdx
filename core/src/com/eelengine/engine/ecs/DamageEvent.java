package com.eelengine.engine.ecs;

import com.artemis.annotations.EntityId;

public class DamageEvent {
    public static int SLASHING=1;
    public static int FIRE=1<<1;
    public static int ICE=1<<2;
    public static int AIR=1<<3;
    public static int WATER=1<<4;
    int amt;
    int type;
    //@EntityId
    int source;
    int team;

    public DamageEvent(int amt, int type, int source) {
        this.amt = amt;
        this.type = type;
        this.source = source;
    }

    public boolean isType(int type){
        return (this.type&type)!=0;
    }
}
