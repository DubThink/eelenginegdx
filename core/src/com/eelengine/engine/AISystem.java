package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Handles health objects
 *
 */
public class AISystem extends IteratingSystem {
    SpriteBatch debugBatch;
    ComponentMapper<CHealth> mHealth; // injected automatically.
    ComponentMapper<CTeam> mTeam; // injected automatically.
    ComponentMapper<CNavigator> mNavigator; // injected automatically.
    ComponentMapper<CTransform> mTransform; // injected automatically.

    public AISystem(SpriteBatch debugBatch) {
        super(Aspect.all(CAIControl.class));
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
        CHealth cHealth = mHealth.get(e);
        while (!cHealth.damageEvents.isEmpty()) {
            DamageEvent event = cHealth.damageEvents.poll();
            int otherTeam = mTeam.has(event.source)?mTeam.get(event.source).team:0;
            int team = mTeam.has(e)?mTeam.get(e).team:0;
            if(team==otherTeam&&team!=0)continue; // Continue if teams are the same and not 0
            cHealth.health -= event.amt;
        }
        if (cHealth.health <= 0)world.delete(e);
//        if (mHealth.has(e)) {
//            Vector2 pos=camController.toScreen(trans.pos);
//            FontKit.SysHuge.draw(debugBatch,
//                    mHealth.get(e).health + "",
//                    trans.pos.x, trans.pos.y);
//        }
    }
}