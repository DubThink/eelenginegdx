package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

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
        PhysicsComponent physicsComponent=mPhysics.get(e);
        if(physicsComponent.isStunned())return;
        InputComponent inputComponent=mInput.get(e);

    }
}