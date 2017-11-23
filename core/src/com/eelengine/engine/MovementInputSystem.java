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
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    ComponentMapper<CInput> mInput; // injected automatically.
    public MovementInputSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class,CInput.class));
    }

    @Override
    protected void process(int e) {
        CPhysics physics=mPhysics.get(e);
        if(physics.isStunned())return;
        CInput input=mInput.get(e);
        CTransform transform = mTransform.get(e);

        //System.out.println(transform.rotLockedToPhysics+" "+transform.rot);
        if(physics.body.getLinearVelocity().len2()>0.05)
            transform.rot=physics.body.getLinearVelocity().angle()* Util.DEG_TO_RAD_F ;
        float speed=10;
        int xMove=0;
        if(input.checkOn(CInput.LEFT))xMove--;
        if(input.checkOn(CInput.RIGHT))xMove++;
        int yMove=0;
        if(input.checkOn(CInput.UP))yMove++;
        if(input.checkOn(CInput.DOWN))yMove--;
        Vector2 v=new Vector2(xMove,yMove);
        v.setLength(speed);
        physics.body.setLinearVelocity(v);


    }
}