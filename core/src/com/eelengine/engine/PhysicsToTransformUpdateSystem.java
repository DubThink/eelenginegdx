package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * Simple sprite renderer
 */
public class PhysicsToTransformUpdateSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> mTransform; // injected automatically.
    ComponentMapper<PhysicsComponent> mPhysics; // injected automatically.
    public PhysicsToTransformUpdateSystem() {
        super(Aspect.all(PhysicsComponent.class,TransformComponent.class));
    }

    @Override
    protected void process(int e) {
        TransformComponent transform=mTransform.get(e);
        System.out.println("PhysicsToTransformSystem "+e);
        System.out.println(transform);
        System.out.println(mPhysics.get(e).getPos());
        transform.pos=mPhysics.get(e).getPos();
        if(transform.rotLockedToPhysics)transform.rot=mPhysics.get(e).getRot();
    }
}