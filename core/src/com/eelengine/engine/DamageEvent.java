package com.eelengine.engine;

import com.artemis.annotations.EntityId;

public class DamageEvent {
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

    public boolean isType(short type){
        return (this.type&type)!=0;
    }
}
