package com.eelengine.engine.ecs;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.eelengine.engine.EelGame;
import com.eelengine.engine.FontKit;
import com.eelengine.engine.ecs.CMailbox;
import com.eelengine.engine.ecs.CTransform;
import com.eelengine.engine.ecs.CamController;

/**
 * Updates the physics system based on transform.
 */
public class MailSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CMailbox> mMailbox; // injected automatically.
    PolygonSpriteBatch renderBatch;
    CamController controller;
    Texture mailPopup;

    public MailSystem(PolygonSpriteBatch renderBatch, CamController controller) {
        super(Aspect.all(CMailbox.class,CTransform.class));
        this.renderBatch=renderBatch;
        this.controller=controller;
    }

    protected void process(int e) {
        CTransform trans=mTransform.get(e);
        if(mMailbox.get(e).mailCt>0){
            renderBatch.draw(mailPopup,
                    trans.pos.x* EelGame.GSCALE-mailPopup.getWidth()*.5f,
                    trans.pos.y*EelGame.GSCALE-mailPopup.getHeight()*.5f,
                    mailPopup.getWidth()*.5f,
                    mailPopup.getHeight()*.5f,
                    mailPopup.getWidth(),
                    mailPopup.getHeight(),
                    trans.scl.x,
                    trans.scl.y,
                    trans.rot*Util.RAD_TO_DEG_F,
                    0,
                    0,
                    mailPopup.getWidth(),
                    mailPopup.getHeight(),
                    false,
                    false);
        }
        FontKit.SysHuge.setColor(Color.OLIVE);
        FontKit.SysHuge.draw(renderBatch,
                mMailbox.get(e).mailCt + "",
                trans.pos.x*EelGame.GSCALE, trans.pos.y*EelGame.GSCALE);
    }
    @Override
    protected void begin() {
        renderBatch.begin();
    }

    @Override
    protected void end() {
        renderBatch.end();
    }

    public void clearAll(){
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for(int i=0;i<actives.size();i++){
            world.delete(ids[i]);
        }
    }
}