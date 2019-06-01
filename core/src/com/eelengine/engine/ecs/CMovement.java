package com.eelengine.engine.ecs;

import com.artemis.Component;

/**
 * A null class to copy.
 */
public class CMovement extends Component {
    float speed=0;
    boolean vehicular=false;
    float turningRate=1.3f; // turning rate in radians per second per unit speed per second
    float maxSpeed=0;
    float accel =1.6f;
    float deccel = 2.5f;
    float maxTurningRate=1.3f; // max turning rate in radians per second. Not affected by speed

    public CMovement setMaxSpeed(float maxSpeed){
        this.maxSpeed=maxSpeed;
        return this;
    }
    public CMovement setAccel(float accel){
        this.accel = accel;
        return this;
    }
    public CMovement setDeccel(float deccel){
        this.deccel = deccel;
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
    public CMovement setMaxTurningRate(float turningRate) {
        this.maxTurningRate = turningRate;
        return this;
    }
}
