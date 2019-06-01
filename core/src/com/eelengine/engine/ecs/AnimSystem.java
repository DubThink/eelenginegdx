package com.eelengine.engine.ecs;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ecs.CAnim;
import com.eelengine.engine.ecs.COneTex;
import com.eelengine.engine.ecs.CPhysics;

/**
 * Updates the physics system based on transform.
 */
public class AnimSystem extends IteratingSystem {
    ComponentMapper<COneTex> mTex; // injected automatically.
    ComponentMapper<CAnim> mAnim; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
//    PolygonSpriteBatch renderBatch;
//    CamController controller;
//    Texture mailPopup;

    public AnimSystem() {
        super(Aspect.all(COneTex.class,CAnim.class,CPhysics.class));
    }

    protected void process(int e) {
        CAnim anim=mAnim.get(e);
        if(mPhysics.get(e).body.getLinearVelocity().len2()>.1f){
            anim.animTime+=world.delta;
            if(anim.animTime>anim.speed){
                anim.animTime%=anim.speed;
                anim.state++;
                anim.state %=anim.textures.size();
                mTex.get(e).texture=anim.textures.get(anim.state);
            }
        }else if(anim.state!=0){
            mTex.get(e).texture=anim.textures.get(0);
            anim.state=0;
            anim.animTime=0;
        }
    }
}