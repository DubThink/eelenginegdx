package com.eelengine.engine.ecs;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.eelgame.CCharacterAnimation;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;
import com.eelengine.engine.sprite.CSprite;

/**
 * Simple region renderer
 */
public class InputSystem extends IteratingSystem {
    ComponentMapper<CInput> mInput;

    public InputSystem() {
        super(Aspect.all(CInput.class));
    }

    @Override
    protected void process(int e) {

        CInput input=mInput.get(e);
        input.clear();
    }
}