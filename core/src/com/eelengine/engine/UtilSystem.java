package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

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
        if(mProjectile.has(e)&&mTransform.has(e)&&
                !Etil.inBounds(mTransform.get(e).pos,-40,-40,40,40))
            ;//world.delete(e); //FIXME
    }
}