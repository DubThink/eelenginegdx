package com.eelengine.engine.sprite;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.eelengine.engine.StaticSprite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class SpriteInstanceManager implements EntitySubscription.SubscriptionListener {
    ComponentMapper<CSprite> mSpriteInstance;

    ArrayList<SpriteInstance> staticSpriteInstances = new ArrayList<>();
    ArrayList<SpriteInstance> dynamicSpriteInstances = new ArrayList<>();
    ArrayList<AnimatedSpriteInstance> animatedSpriteInstances = new ArrayList<>();

    boolean staticsSorted=true;
    public SpriteInstanceManager(World entityWorld){
        mSpriteInstance = entityWorld.getMapper(CSprite.class);
    }

    public void addSpriteInstance(SpriteInstance instance){
        dynamicSpriteInstances.add(instance);
        if(instance instanceof AnimatedSpriteInstance)
            animatedSpriteInstances.add((AnimatedSpriteInstance) instance);
    }

    public void removeSpriteInstance(SpriteInstance instance){
        instance.markToRemove();
    }

    public void addStaticSpriteInstance(SpriteInstance instance){
        staticSpriteInstances.add(instance);
        if(instance instanceof AnimatedSpriteInstance)
            animatedSpriteInstances.add((AnimatedSpriteInstance) instance);
        if(staticSpriteInstances.size()<2)return;
        if(SpriteInstance.renderOrderComparator.compare(staticSpriteInstances.get(staticSpriteInstances.size()-2),instance)>0)staticsSorted=false;
    }

    @Override
    public void inserted(IntBag entities) {
        for(int i=0;i<entities.size();i++) {
            int e = entities.get(i);
            addSpriteInstance(mSpriteInstance.get(e).getInstance());
        }
    }

    @Override
    public void removed(IntBag entities) {
        for(int i=0;i<entities.size();i++) {
            int e = entities.get(i);
            removeSpriteInstance(mSpriteInstance.get(e).getInstance());
        }
    }

    public void updateAnimations(float dt){
        for(AnimatedSpriteInstance instance: animatedSpriteInstances){
            instance.update(dt);
        }
    }

    private void sortDynamic(){
        dynamicSpriteInstances.sort(SpriteInstance.renderOrderComparator);
    }

    private void sortStatic(){
        System.out.print("Sorting statics...");
        staticSpriteInstances.sort(SpriteInstance.renderOrderComparator);
        System.out.println("sorted");
        staticsSorted=true;
    }

    public void render(Batch batch){
        sortDynamic();
        if(!staticsSorted)sortStatic();
        if(dynamicSpriteInstances.size()==0) {
            for (SpriteInstance instance : staticSpriteInstances)
                instance.render(batch);
            return;
        }
        if(staticSpriteInstances.size()==0) {
            for (SpriteInstance instance : dynamicSpriteInstances)
                instance.render(batch);
            return;
        }
        PeekableIterator<SpriteInstance> dynamics = new PeekableIterator<>(dynamicSpriteInstances.iterator());
        PeekableIterator<SpriteInstance> statics = new PeekableIterator<>(staticSpriteInstances.iterator());
        while(dynamics.hasNext()&&statics.hasNext()){
            if(SpriteInstance.renderOrderComparator.compare(dynamics.peekNext(),statics.peekNext())>=0){
                // render the static
                statics.next().render(batch);
            } else {
                // render the dynamic
                dynamics.next().render(batch);
            }
        }

        dynamics.forEachRemaining(instance -> instance.render(batch));
        statics.forEachRemaining(instance -> instance.render(batch));

    }
}
