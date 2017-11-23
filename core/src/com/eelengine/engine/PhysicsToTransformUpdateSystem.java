package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * Simple sprite renderer
 */
public class PhysicsToTransformUpdateSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    public PhysicsToTransformUpdateSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class));
    }

    @Override
    protected void process(int e) {
        CTransform transform=mTransform.get(e);
        transform.pos=mPhysics.get(e).getPos();
        if(transform.rotLockedToPhysics)transform.rot=mPhysics.get(e).getRot();
    }
}