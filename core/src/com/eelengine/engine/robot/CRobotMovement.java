package com.eelengine.engine.robot;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * A robot's movement (the 'chassis')
 */
public class CRobotMovement extends Component {
    /* units per second */
    public float movementSpeed = 2.0f;
    /* radians per second */
    public float rotationSpeed = 4.0f;
    Vector2 desiredDir=new Vector2(1,0);
    Vector2 desiredPos=new Vector2();
    // controls if the robot is moving on its own
    boolean isMoving=false;
    public CRobotMovement setDesiredDirection(Vector2 dir){
        this.desiredDir=dir;
        return this;
    }
    public CRobotMovement setDesiredPosition(Vector2 pos){
        this.desiredPos=pos;
        isMoving=true;
        return this;
    }


}
