package com.eelengine.engine;

import com.artemis.ComponentMapper;
import com.artemis.World;

/**
 * Container class for {@link ComponentMapper}s.
 * {@link #initialize(World)} must be called after the entity world is started.
 */
public class ECS {
    public static ComponentMapper<CGraphics> mGraphics;
    public static ComponentMapper<CTransform> mTransform;
    public static ComponentMapper<CPhysics> mPhysics;
    public static ComponentMapper<CInput> mInput;

    /**
     * Initializes the {@link ComponentMapper}s.
     * @param entityWorld The entity world to
     */
    public static void initialize(World entityWorld){
        mGraphics=entityWorld.getMapper(CGraphics.class);
        mTransform =entityWorld.getMapper(CTransform.class);
        mPhysics=entityWorld.getMapper(CPhysics.class);
        mInput=entityWorld.getMapper(CInput.class);
    }
}
