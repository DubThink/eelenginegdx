package com.eelengine.engine.ecs;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.eelengine.engine.EelGame;
import com.eelengine.engine.FontKit;

/**
 * Simple region renderer
 */
public class RenderOneTexSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<COneTex> mGraphics; // injected automatically.
    ComponentMapper<CDamageable> mHealth; // injected automatically.
    PolygonSpriteBatch renderBatch;
    public RenderOneTexSystem(PolygonSpriteBatch renderBatch) {
        super(Aspect.all(COneTex.class,CTransform.class));
        assert renderBatch!=null;
        this.renderBatch=renderBatch;
    }
    
    @Override
    protected void begin() {
        renderBatch.begin();
    }

    @Override
    protected void end() {
        renderBatch.end();
    }

    @Override
    protected void process(int e) {
        CTransform trans= mTransform.get(e);
        COneTex tex=mGraphics.get(e);
        Texture texture=tex.texture;
        renderBatch.draw(texture,
                trans.pos.x* EelGame.GSCALE-texture.getWidth()*tex.offsetX,
                trans.pos.y*EelGame.GSCALE-texture.getHeight()*tex.offsetY,
                texture.getWidth()*tex.offsetX,
                texture.getHeight()*tex.offsetY,
                texture.getWidth(),
                texture.getHeight(),
                trans.scl.x,
                trans.scl.y,
                trans.rot*Util.RAD_TO_DEG_F,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false);
        if(mHealth.has(e)){
            FontKit.SysHuge.setColor(Color.FIREBRICK);
            FontKit.SysHuge.draw(renderBatch,
                    mHealth.get(e).health + "",
                    trans.pos.x*EelGame.GSCALE, trans.pos.y*EelGame.GSCALE);
        }
    }
}