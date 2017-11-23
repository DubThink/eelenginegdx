package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * Updates the physics system based on transform.
 */
public class PhysicsSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    public PhysicsSystem() {
        super(Aspect.all(CPhysics.class,CTransform.class));
    }

    @Override
    protected void process(int e) {
        CTransform transform=mTransform.get(e);
        CPhysics physics=mPhysics.get(e);
        if(physics.isStunned())physics.stunnedFor= Util.max(0,physics.stunnedFor-world.getDelta());
        physics.body.setTransform(transform.pos,transform.rotLockedToPhysics?transform.rot:physics.body.getAngle());
    }
}