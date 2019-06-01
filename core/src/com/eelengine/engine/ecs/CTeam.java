package com.eelengine.engine.ecs;

import com.artemis.Component;

/**
 * A null class to copy.
 */
public class CTeam extends Component {
    int team=0;
    public CTeam set(int team){
        this.team=team;
        return this;
    }
}
