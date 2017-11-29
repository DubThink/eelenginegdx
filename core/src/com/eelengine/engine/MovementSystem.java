package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple sprite renderer
 */
public class MovementSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    ComponentMapper<CInput> mInput; // injected automatically.
    ComponentMapper<CNavigator> mNavigator; // injected automatically.
    public MovementSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class).one(CInput.class,CNavigator.class));
    }

    @Override
    protected void process(int e) {
        CPhysics physics=mPhysics.get(e);
        if(physics.isStunned())return;
        CTransform transform = mTransform.get(e);
        Vector2 v=new Vector2();
        if(mInput.has(e)&&mInput.get(e).enabled) {
            CInput input = mInput.get(e);

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
        }else if(mNavigator.has(e)){
            if (physics.body.getLinearVelocity().len2() > 0.5) // TODO make controlled by a component param (or remove)
                transform.rot = physics.body.getLinearVelocity().angle() * Util.DEG_TO_RAD_F;
            v.set(mNavigator.get(e).desiredMove);
        }
        float speed = 4; //TODO move to a component
        v.setLength(speed);
//        System.out.println("New move v "+v);
        physics.body.setLinearVelocity(v);


    }
}