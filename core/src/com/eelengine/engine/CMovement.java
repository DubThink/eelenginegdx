package com.eelengine.engine;

import com.artemis.Component;

/**
 * A null class to copy.
 */
public class CMovement extends Component {
    float speed=0;
    boolean vehicular=false;
    float turningRate=0;
    public CMovement setSpeed(float speed){
        this.speed=speed;
        return this;
    }

    public CMovement setVehicular(boolean vehicular) {
        this.vehicular = vehicular;
        return this;
    }

    public CMovement setTurningRate(float turningRate) {
        this.turningRate = turningRate;
        return this;
    }
}
