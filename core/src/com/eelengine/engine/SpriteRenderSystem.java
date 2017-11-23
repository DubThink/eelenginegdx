package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Simple sprite renderer
 */
public class SpriteRenderSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CGraphics> mGraphics; // injected automatically.
    SpriteBatch renderBatch;
    public SpriteRenderSystem(SpriteBatch renderBatch) {
        super(Aspect.all(CGraphics.class,CTransform.class));
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
        Texture texture=mGraphics.get(e).texture;
        renderBatch.draw(texture,
                trans.pos.x*EelGame.GSCALE-texture.getWidth()*.5f,
                trans.pos.y*EelGame.GSCALE-texture.getHeight()*.5f,
                texture.getWidth()*.5f,
                texture.getHeight()*.5f,
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
    }
}