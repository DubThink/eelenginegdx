package com.eelengine.engine.sprite;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ecs.CInput;
import com.eelengine.engine.ecs.CTransform;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;
import com.eelengine.engine.sprite.CSprite;

/**
 * Simple region renderer
 */
public class SpriteSystem extends IteratingSystem {
    ComponentMapper<CSprite> mSprite; // injected automatically.
    ComponentMapper<CTransform> mTransform;
    public SpriteSystem() {
        super(Aspect.all(CSprite.class,CTransform.class));
    }

    @Override
    protected void process(int e) {
        CTransform transform = mTransform.get(e);
        mSprite.get(e).getInstance().setPos(transform.pos);
    }
}