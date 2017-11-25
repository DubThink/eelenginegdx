package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ai.Navigation;

/**
 * Updates the physics system based on transform.
 */
public class NavigationSystem extends IteratingSystem {
    ComponentMapper<CNavigator> mNavigator; // injected automatically.
    ComponentMapper<CTransform> mTransform; // injected automatically.
//    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    Navigation navigation;
    public NavigationSystem(Navigation navigation) {
        super(Aspect.all(CNavigator.class,CTransform.class));
        this.navigation=navigation;
    }

    @Override
    protected void process(int e) {
        CNavigator navigator=mNavigator.get(e);
        if(navigator.targetMode==CNavigator.POINT){
            if(navigator.path!=null&&
                    navigator.path.targetX==navigator.target.x&&
                    navigator.path.targetY==navigator.target.y){
                // Path is good
            } else {
                navigator.path = navigation.findPath();
            }
        }
    }
}