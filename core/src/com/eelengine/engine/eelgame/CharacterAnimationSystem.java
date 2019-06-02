package com.eelengine.engine.eelgame;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.eelengine.engine.ecs.CDamageable;
import com.eelengine.engine.ecs.CInput;
import com.eelengine.engine.ecs.DamageEvent;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;
import com.eelengine.engine.sprite.CSprite;

/**
 * Simple region renderer
 */
public class CharacterAnimationSystem extends IteratingSystem {
    ComponentMapper<CSprite> mSprite; // injected automatically.
    ComponentMapper<CCharacterAnimation> mCharacterAnimation;
    ComponentMapper<CInput> mInput;

    public CharacterAnimationSystem() {
        super(Aspect.all(CSprite.class,CCharacterAnimation.class,CInput.class));
    }

    @Override
    protected void process(int e) {
        AnimatedSpriteInstance spriteInstance=mSprite.get(e).getAnimated();
        if(spriteInstance==null)return;
        CInput input=mInput.get(e);
        CCharacterAnimation animation = mCharacterAnimation.get(e);
        if(input.checkDown(CInput.DOWN))
            spriteInstance.playSequence(animation.dSeq);
        if(input.checkDown(CInput.UP))
            spriteInstance.playSequence(animation.uSeq);
        if(input.checkDown(CInput.LEFT))
            spriteInstance.playSequence(animation.lSeq);
        if(input.checkDown(CInput.RIGHT))
            spriteInstance.playSequence(animation.rSeq);
        /* if a movement key moved up and there are no movement keys on, start idle */
        if(input.checkUp(CInput.MOVEMENT)&&!input.checkOn(CInput.MOVEMENT))
            spriteInstance.playSequence(animation.idleSeq);
    }
}