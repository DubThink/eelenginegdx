package com.eelengine.engine.sprite;

import com.artemis.Component;

import java.util.Iterator;
import java.util.LinkedList;

public class CSprite extends Component{
    private SpriteInstance instance;
    private boolean animated;
    public CSprite setInstance(SpriteInstance instance) {
        this.instance = instance;
        animated = instance instanceof AnimatedSpriteInstance;
        return this;
    }

    public SpriteInstance getInstance() {
        return instance;
    }

    /**
     * Returns the instance if the instance is animated
     * Otherwise, returns null
     * @return
     */
    public AnimatedSpriteInstance getAnimated(){
        if(animated)return (AnimatedSpriteInstance)instance;
        return null;
    }
}
