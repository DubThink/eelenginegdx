package com.eelengine.engine.sprite;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class SpriteInstanceManager implements EntitySubscription.SubscriptionListener {
    ComponentMapper<CSpriteInstances> mSpriteInstances;

    ArrayList<SpriteInstance> spriteInstances = new ArrayList<>();
    ArrayList<AnimatedSpriteInstance> animatedSpriteInstances = new ArrayList<>();

    public SpriteInstanceManager(World entityWorld){
        mSpriteInstances = entityWorld.getMapper(CSpriteInstances.class);
    }

    public void addSpriteInstance(SpriteInstance instance){
        spriteInstances.add(instance);
        if(instance instanceof AnimatedSpriteInstance)
            animatedSpriteInstances.add((AnimatedSpriteInstance) instance);
    }

    public void removeSpriteInstance(SpriteInstance instance){
        instance.markToRemove();
    }

    @Override
    public void inserted(IntBag entities) {
        for(int i=0;i<entities.size();i++) {
            int e = entities.get(i);
            for (SpriteInstance spriteInstance : mSpriteInstances.get(e).instances)
                addSpriteInstance(spriteInstance);
        }
    }

    @Override
    public void removed(IntBag entities) {
        for(int i=0;i<entities.size();i++) {
            int e = entities.get(i);
            for (SpriteInstance spriteInstance : mSpriteInstances.get(e).instances)
                removeSpriteInstance(spriteInstance);
        }
    }

    public void updateAnimations(float dt){
        for(AnimatedSpriteInstance instance: animatedSpriteInstances){
            instance.update(dt);
        }
    }
    public void render(Batch batch){
        for(SpriteInstance instance: spriteInstances){
            instance.render(batch);
        }
    }
}
