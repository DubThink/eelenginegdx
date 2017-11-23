package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple sprite renderer
 */
public class MovementInputSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> mTransform; // injected automatically.
    ComponentMapper<PhysicsComponent> mPhysics; // injected automatically.
    ComponentMapper<InputComponent> mInput; // injected automatically.
    public MovementInputSystem() {
        super(Aspect.all(PhysicsComponent.class,TransformComponent.class,InputComponent.class));
    }

    @Override
    protected void process(int e) {
        PhysicsComponent physics=mPhysics.get(e);
        if(physics.isStunned())return;
        InputComponent input=mInput.get(e);
        TransformComponent transform = mTransform.get(e);

        //System.out.println(transform.rotLockedToPhysics+" "+transform.rot);
        if(physics.body.getLinearVelocity().len2()>0.05)
            transform.rot=physics.body.getLinearVelocity().angle()* Util.DEG_TO_RAD_F ;
        float speed=10;
        int xMove=0;
        if(input.checkOn(InputComponent.LEFT))xMove--;
        if(input.checkOn(InputComponent.RIGHT))xMove++;
        int yMove=0;
        if(input.checkOn(InputComponent.UP))yMove++;
        if(input.checkOn(InputComponent.DOWN))yMove--;
        Vector2 v=new Vector2(xMove,yMove);
        v.setLength(speed);
        physics.body.setLinearVelocity(v);


    }
}