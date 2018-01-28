package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple region renderer
 */
public class MovementSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    ComponentMapper<CInput> mInput; // injected automatically.
    ComponentMapper<CNavigator> mNavigator; // injected automatically.
    ComponentMapper<CMovement> mMovement; // injected automatically.

    public MovementSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class,CMovement.class).one(CInput.class,CNavigator.class));
    }

    @Override
    protected void process(int e) {
        CPhysics physics=mPhysics.get(e);
        if(physics.isStunned())return;
        CTransform transform = mTransform.get(e);
        CMovement movement=mMovement.get(e);
        Vector2 v=new Vector2();
        boolean coast=false;
        if(mInput.has(e)&&mInput.get(e).enabled) {
            CInput input = mInput.get(e);
            if(movement.vehicular){
                int xMove = 0;
                if (input.checkOn(CInput.LEFT)) xMove--;
                if (input.checkOn(CInput.RIGHT)) xMove++;
                int yMove = 0;
                if (input.checkOn(CInput.UP)) yMove++;
                if (input.checkOn(CInput.DOWN)) yMove--;
                Vector2 desired=new Vector2(xMove,yMove);
                Vector2 current=new Vector2(1,0);
                current.setAngleRad(transform.rot);
                float rate=Util.min(movement.turningRate*movement.speed,movement.maxTurningRate)*world.delta;
                coast=!(input.checkOn(CInput.LEFT)||input.checkOn(CInput.RIGHT)||input.checkOn(CInput.UP)||input.checkOn(CInput.DOWN));

                //System.out.printf("Desired angle:%.3f Current angle:%.3f Rate:%.3f\n",desired.angle(),current.angle(),rate);
                System.out.println(current.angleRad(desired)+" "+rate*1.5);
                if(!desired.isZero()&&Util.abs(current.angleRad(desired))<rate*1.5){
                    transform.rot=desired.angle()*Util.DEG_TO_RAD_F;
                    v.set(yMove,xMove);//these don't matter this is only to keep it from moving when both are 0
                } else if(current.dot(desired)<-0.4) {//desired is in opposite direction; set speed to 0
//                    System.out.println("Decel");
                    v.setZero();
                } else {
                    float delta=desired.angle()-current.angle();
                    boolean left=delta<0;
                    if (Util.abs(delta) > 180) {
                        left=!left;
                    }
                    //System.out.println("Rotate "+(left?"left":"right"));
                    if(yMove!=0||xMove!=0)transform.rot+=left?-rate:rate;
                    v.set(yMove,xMove);//these don't matter this is only to keep it from moving when both are 0
                }
                v.setAngleRad(transform.rot);
            }else{
                //NON VEHICULAR MOVEMENT
                //System.out.println(transform.rotLockedToPhysics+" "+transform.rot);
                if (physics.body.getLinearVelocity().len2() > 0.5) // TODO make controlled by a component param (or remove)
                    transform.rot = physics.body.getLinearVelocity().angle() * Util.DEG_TO_RAD_F;
                int xMove = 0;
                if (input.checkOn(CInput.LEFT)) xMove--;
                if (input.checkOn(CInput.RIGHT)) xMove++;
                int yMove = 0;
                if (input.checkOn(CInput.UP)) yMove++;
                if (input.checkOn(CInput.DOWN)) yMove--;
                v.set(xMove, yMove);
            }
        }else if(mNavigator.has(e)){
            if (physics.body.getLinearVelocity().len2() > 0.5) // TODO make controlled by a component param (or remove)
                transform.rot = physics.body.getLinearVelocity().angle() * Util.DEG_TO_RAD_F;
            v.set(mNavigator.get(e).desiredMove);
        }

//        System.out.p rintf("speed %.3f  accel factor %.3f max speed %.3f\n",movement.speed,movement.accel*world.delta,movement.maxSpeed);
        //System.out.println("coast: "+coast);
        if(!coast) {
            if (v.isZero()) movement.speed = Util.max(0, movement.speed - movement.deccel * world.delta);
            else movement.speed = Util.min(movement.maxSpeed, movement.speed + movement.accel * world.delta);
        }
        if(movement.vehicular)v.set(0,1).setAngleRad(transform.rot);
        v.setLength(movement.speed);
//        System.out.println("New move v "+v);
        physics.body.setLinearVelocity(v);


    }
}