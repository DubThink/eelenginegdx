package com.eelengine.engine.robot;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ecs.CTransform;
import com.eelengine.engine.GridWorld;
import com.eelengine.engine.SergeiGame;

/**
 * Simple region renderer
 */
public class RobotMovementSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CRobotMovement> mRobotMovement; // injected automatically.
    ComponentMapper<CRobot> mRobot; // injected automatically.
    public GridWorld gridWorld;
    public SergeiGame game;

    public RobotMovementSystem(GridWorld gridWorld, SergeiGame game) {
        super(Aspect.all(CRobot.class,CTransform.class, CRobotMovement.class));
        this.gridWorld=gridWorld;
        this.game=game;
    }

    @Override
    protected void process(int e) {
        CRobotMovement movement = mRobotMovement.get(e);
        CTransform transform = mTransform.get(e);
        CRobot robot = mRobot.get(e);

        float rotamt=movement.rotationSpeed*world.delta;
        float movamt=world.delta * movement.movementSpeed;
        transform.rot=((transform.rot%Util.TWO_PI_F)+Util.TWO_PI_F)%Util.TWO_PI_F;
        float currentAngle=transform.rot;
        float desiredAngle=movement.desiredDir.angle()*Util.DEG_TO_RAD_F;
        if(Math.abs(currentAngle-desiredAngle)<rotamt) {
            transform.rot=desiredAngle;
            if(!movement.isMoving)return;
            if (transform.pos.dst2(movement.desiredPos) > 0.2*movamt )
                transform.pos.add(movement.desiredDir.nor().scl(movamt));
            else{
                movement.isMoving=false;
                transform.pos.set(movement.desiredPos);
            }
        } else{
            if(desiredAngle-currentAngle>Util.PI_F||(desiredAngle-currentAngle<0&&desiredAngle-currentAngle>-Util.PI_F)){
                // rotate ccw
                transform.rot-=rotamt;
            } else {
                transform.rot+=rotamt;
            }
        }
    }
}