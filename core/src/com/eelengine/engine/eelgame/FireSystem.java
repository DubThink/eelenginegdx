package com.eelengine.engine.eelgame;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.ecs.CDamageable;
import com.eelengine.engine.ecs.DamageEvent;
import com.eelengine.engine.eelgame.CFire;
import com.eelengine.engine.sprite.CSpriteInstances;

/**
 * Simple region renderer
 */
public class FireSystem extends IteratingSystem {
    ComponentMapper<CDamageable> mDamageable; // injected automatically.
    ComponentMapper<CFire> mFire;

    public FireSystem() {
        super(Aspect.all(CDamageable.class, CFire.class));
    }

    @Override
    protected void process(int e) {
        CFire fire = mFire.get(e);
        boolean wasLit=fire.lit;
        for(DamageEvent event:mDamageable.get(e).getDamageEvents()){
            if(event.isType(DamageEvent.FIRE))
                fire.lit=true;
            if(event.isType(DamageEvent.ICE|DamageEvent.AIR|DamageEvent.WATER))
                fire.lit=false;
        }
        if(fire.lit&&!wasLit){
            // light the fire
            if(fire.spriteInstance!=null)fire.spriteInstance.playSequence("lit");
        }
        if(!fire.lit&&wasLit){
            // light the fire
            if(fire.spriteInstance!=null)fire.spriteInstance.playSequence("unlit");
        }
    }
}