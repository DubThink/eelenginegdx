package com.eelengine.engine;

import com.artemis.ComponentMapper;
import com.artemis.World;

/**
 * Container class for {@link ComponentMapper}s.
 * {@link #initialize(World)} must be called after the entity world is started.
 */
public class ECS {
    public static ComponentMapper<GraphicsComponent> mGraphics;
    public static ComponentMapper<TransformComponent> mTransform;
    public static ComponentMapper<PhysicsComponent> mPhysics;
    public static ComponentMapper<InputComponent> mInput;

    /**
     * Initializes the {@link ComponentMapper}s.
     * @param entityWorld The entity world to
     */
    public static void initialize(World entityWorld){
        mGraphics=entityWorld.getMapper(GraphicsComponent.class);
        mTransform =entityWorld.getMapper(TransformComponent.class);
        mPhysics=entityWorld.getMapper(PhysicsComponent.class);
        mInput=entityWorld.getMapper(InputComponent.class);
    }
}
