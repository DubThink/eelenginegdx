package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Handles health objects
 *
 */
public class HealthSystem extends IteratingSystem {
    SpriteBatch debugBatch;
    CamController camController;
    ComponentMapper<CHealth> mHealth; // injected automatically.

    public HealthSystem(SpriteBatch debugBatch,CamController camController) {
        super(Aspect.all(CHealth.class));
        this.debugBatch = debugBatch;
        this.camController=camController;
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