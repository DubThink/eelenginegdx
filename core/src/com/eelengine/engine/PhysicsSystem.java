package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * Updates the physics system based on transform.
 */
public class PhysicsSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> mTransform; // injected automatically.
    ComponentMapper<PhysicsComponent> mPhysics; // injected automatically.
    public PhysicsSystem() {
        super(Aspect.all(PhysicsComponent.class,TransformComponent.class));
    }

    @Override
    protected void process(int e) {
        TransformComponent transform=mTransform.get(e);
        PhysicsComponent physics=mPhysics.get(e);
        if(physics.isStunned())physics.stunnedFor= Util.max(0,physics.stunnedFor-world.getDelta());
        physics.body.setTransform(transform.pos,transform.rotLockedToPhysics?transform.rot:physics.body.getAngle());
    }
}