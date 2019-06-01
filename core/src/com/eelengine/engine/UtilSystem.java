package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ecs.CHealth;
import com.eelengine.engine.ecs.CProjectile;
import com.eelengine.engine.ecs.CTransform;

/**
 * Assorted stuff that doesn't have a better place to be YET
 * //TODO find homes for these functionalities
 */
public class UtilSystem extends IteratingSystem {
    ComponentMapper<CHealth> mHealth; // injected automatically.
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CProjectile> mProjectile; // injected automatically.
    //ComponentMapper<CPhysics> mPhysics; // injected automatically.
    public UtilSystem() {
        super(Aspect.all(CProjectile.class));
    }

    @Override
    protected void process(int e) {
        mProjectile.get(e).age+=world.delta;
        if(mProjectile.get(e).lifetime>0&&mProjectile.get(e).lifetime<=mProjectile.get(e).age){
            world.delete(e);
        } else if(/*mProjectile.has(e)&&*/mTransform.has(e)&&
            !Etil.inBounds(mTransform.get(e).pos,-40,-40,40,40)) {
            world.delete(e);
        }
    }
}