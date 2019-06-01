package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * Simple region renderer
 */
public class PhysicsToTransformUpdateSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    public PhysicsToTransformUpdateSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class));
    }

//    @Override
//    protected void begin() {
//    }
//
//    @Override
//    protected void end() {
//        System.out.print("PTT end");
//    }

    @Override
    protected void process(int e) {
        CTransform transform=mTransform.get(e);
        transform.pos=mPhysics.get(e).getPos();
        if(transform.rotLockedToPhysics)transform.rot=mPhysics.get(e).getRot();
        else if(transform.pointAtVelocity&&mPhysics.get(e).body.getLinearVelocity().len2()>0.1)
            transform.rot=mPhysics.get(e).body.getLinearVelocity().angle()* Util.DEG_TO_RAD_F-90;
    }
}