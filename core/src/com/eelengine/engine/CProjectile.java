package com.eelengine.engine;

import com.artemis.Component;

/**
 * Defines projectile-related attributes
 */
public class CProjectile extends Component {
    boolean destroyOnHit=true;
    float age=0;
    float lifetime =-1;//-1 lives forever

    /**
     * Sets the maximum lifetime of the projectile. non-positive values live forever.
     */
    public CProjectile setLifetime(float lifetime){
        this.lifetime = lifetime;
        return this;
    }
}
