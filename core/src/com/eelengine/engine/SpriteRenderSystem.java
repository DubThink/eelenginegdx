package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple sprite renderer
 */
public class SpriteRenderSystem extends IteratingSystem {
    ComponentMapper<PositionComponent> mPosition; // injected automatically.
    ComponentMapper<GraphicsComponent> mGraphics; // injected automatically.
    SpriteBatch renderBatch;
    public SpriteRenderSystem(SpriteBatch renderBatch) {
        super(Aspect.all(GraphicsComponent.class,PositionComponent.class));
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
        Vector2 pos=mPosition.get(e).position;
        renderBatch.draw(mGraphics.get(e).texture,pos.x*EelGame.GSCALE,pos.y*EelGame.GSCALE);
    }
}