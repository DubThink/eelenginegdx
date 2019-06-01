package com.eelengine.engine.sprite;

import com.artemis.Component;

import java.util.Iterator;
import java.util.LinkedList;

public class CSpriteInstances extends Component implements Iterable<SpriteInstance>{
    LinkedList<SpriteInstance> instances;
    SpriteInstanceManager manager;

    public void addSpriteInstance(SpriteInstance instance){
        instances.add(instance);
        manager.addSpriteInstance(instance);
    }
    public void removeSpriteInstance(SpriteInstance instance){
        instance.markToRemove();
        manager.removeSpriteInstance(instance);
    }

    @Override
    public Iterator<SpriteInstance> iterator() {
        return instances.iterator();
    }
}
