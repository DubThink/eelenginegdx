package com.eelengine.engine;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Sprite for in-world rendering. Contains transform, so not for use with ECS
 */
public class StaticSprite implements Serializable{
    private static final long serialVersionUID = 6148310914426707246L;
    LoadedTextureRegion region;
    Vector2 pos=new Vector2(0,0);
    transient boolean selected=false;

    public StaticSprite(String name){
        region=new LoadedTextureRegion(name);
    }

    public StaticSprite(String name, Vector2 pos) {
        this(name);
        this.pos.set(pos);
    }
    public StaticSprite(String name, float x, float y) {
        this(name);
        this.pos.set(x,y);
    }
}
