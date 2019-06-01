package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;

/**
 * Updates the physics system based on transform.
 */
public class TriggerSystem extends BaseEntitySystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CTrigger> mTrigger; // injected automatically.
    public TriggerSystem() {
        super(Aspect.all(CTrigger.class,CTransform.class));
    }

    @Override
    protected void processSystem() {
//        CTransform transform=mTransform.get(e);
//        CPhysics physics=mPhysics.get(e);
//        if(physics.isStunned())physics.stunnedFor= Util.max(0,physics.stunnedFor-world.getDelta());
//        physics.body.setTransform(transform.pos,transform.rotLockedToPhysics?transform.rot:physics.body.getAngle());
    }
    /**
     * Tests if point (x,y) overlaps a trigger with the specified flag
     * @param flag the flag to check
     * @param vec world pos
     * @return The first entity with the given flag
     */
    public int checkFlag(String flag, Vector2 vec){
        return checkFlag(flag,vec.x,vec.y);
    }
    /**
     * Tests if point (x,y) overlaps a trigger with the specified flag
     * @param flag the flag to check
     * @param x world x
     * @param y world y
     * @return The first entity with the given flag
     */
    public int checkFlag(String flag, float x, float y){
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
//        System.out.printf("Number of trigger entities %d. world loc %.3f,%.3f\n",actives.size(),x,y);
        for (int i = 0, s = actives.size(); s > i; i++) {
            Vector2 pos=mTransform.get(ids[i]).pos;
            CTrigger trigger=mTrigger.get(ids[i]);
            if(trigger.mode==CTrigger.RADIUS) {
                if (Util.dist2(pos.x, pos.y, x, y) < trigger.r * trigger.r&&trigger.checkFlag(flag)) return ids[i];
            }else {
                if(Util.inBox(x,y,pos.x-trigger.width/2,pos.y-trigger.height/2,pos.x+trigger.width/2,pos.y+trigger.height/2)&&
                        trigger.checkFlag(flag))
                    return ids[i];
            }
        }
        return -1;
    }
    public void test(){
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            System.out.println("Entity @ "+mTransform.get(ids[i]));
        }
    }
}