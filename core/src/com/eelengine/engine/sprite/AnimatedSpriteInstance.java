package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSpriteInstance extends SpriteInstance {
    int state=0;
    float clock=0;
    AnimatedSprite.Sequence sequence=null;

    public AnimatedSpriteInstance(AnimatedSprite sprite, int x, int y){
        super(sprite,x,y);
        if(sprite.getDefaultSequence()!=null)playSequence(sprite.getDefaultSequence());
    }

    public AnimatedSprite getSprite(){
        return (AnimatedSprite)super.getSprite();
    }

    /**
     * Advances the state of the animation once
     */
    public void advance(){
        state++;
    }

    public void update(float dt) {
        if (sequence == null) {
            idx = 0;
            return;
        }
        float time=sequence.getTime(state);
        if (time > 0) {
            clock+=dt;
            if(clock>time) {
                clock -= time;
                state++;
            }
            state %= sequence.sprites.length;

        }
        idx=sequence.getSprite(state);
    }

    public void playSequence(String id){
        clock=0;
        state=0;
        sequence = getSprite().getSequence(id);
    }

    @Override
    public String toString() {
        return String.format("AnimatedSpriteInstance[state=%d,idx=%d,sequence=%s",state,idx,sequence.toString());
    }
}
