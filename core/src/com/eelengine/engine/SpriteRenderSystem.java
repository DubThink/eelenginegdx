package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple sprite renderer
 */
public class SpriteRenderSystem extends IteratingSystem {
    ComponentMapper<TransformComponent> mTransform; // injected automatically.
    ComponentMapper<GraphicsComponent> mGraphics; // injected automatically.
    SpriteBatch renderBatch;
    public SpriteRenderSystem(SpriteBatch renderBatch) {
        super(Aspect.all(GraphicsComponent.class,TransformComponent.class));
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
        TransformComponent trans= mTransform.get(e);
        Texture texture=mGraphics.get(e).texture;
        renderBatch.draw(texture,
                trans.pos.x*EelGame.GSCALE-texture.getWidth()*.5f,
                trans.pos.y*EelGame.GSCALE-texture.getHeight()*.5f,
                texture.getWidth()*.5f,
                texture.getHeight()*.5f,
                texture.getWidth(),
                texture.getHeight(),
                1,
                1,
                trans.rot*Util.RAD_TO_DEG_F,
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false);
    }
}