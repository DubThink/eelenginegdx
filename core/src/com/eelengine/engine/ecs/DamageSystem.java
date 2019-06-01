package com.eelengine.engine.ecs;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eelengine.engine.CamController;

/**
 * Handles health objects
 *
 */
public class DamageSystem extends IteratingSystem {
    SpriteBatch debugBatch;
    ComponentMapper<CDamageable> mHealth; // injected automatically.
    ComponentMapper<CTeam> mTeam; // injected automatically.

    public DamageSystem(SpriteBatch debugBatch) {
        super(Aspect.all(CDamageable.class));
        this.debugBatch = debugBatch;
    }

    @Override
    protected void begin() {
        debugBatch.begin();
    }

    @Override
    protected void end() {
        debugBatch.end();
    }


    @Override
    protected void process(int e) {
        CDamageable cDamageable = mHealth.get(e);
        while (!cDamageable.damageEvents.isEmpty()) {
            DamageEvent event = cDamageable.damageEvents.poll();
            int otherTeam = mTeam.has(event.source)?mTeam.get(event.source).team:0;
            int team = mTeam.has(e)?mTeam.get(e).team:0;
            if(team==otherTeam&&team!=0)continue; // Continue if teams are the same and not 0
            cDamageable.health -= event.amt;
        }
        if (cDamageable.health <= 0)world.delete(e);
//        if (mHealth.has(e)) {
//            Vector2 pos=camController.toScreen(trans.pos);
//            FontKit.SysHuge.draw(debugBatch,
//                    mHealth.get(e).health + "",
//                    trans.pos.x, trans.pos.y);
//        }
    }
}