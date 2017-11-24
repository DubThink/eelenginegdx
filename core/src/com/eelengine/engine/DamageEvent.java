package com.eelengine.engine;

import com.artemis.annotations.EntityId;

public class DamageEvent {
    int amt;
    int type;
//    @EntityId
//    int source;
    //int team;

    public DamageEvent(int amt, int type) {
        this.amt = amt;
        this.type = type;
        //this.team = team;
    }
}
