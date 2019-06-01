package com.eelengine.engine;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Sprite for in-world rendering. Contains transform, so not for use with ECS
 */
public class StaticSprite implements Serializable {
    private static final long serialVersionUID = 6148310914426707249L;
    public LoadedTextureRegion region;
    public Vector2 pos = new Vector2(0, 0);
    public float rot=0;
    public transient boolean selected = false;
    public transient boolean pSelected = false;

    public StaticSprite(String name) {
        region = new LoadedTextureRegion(name.replace("\\", "/"));
    }
    public StaticSprite(StaticSprite sprite){
        this.pos.set(sprite.pos);
        this.region=sprite.region;
    }
    public StaticSprite(StaticSprite sprite, float x, float y){
        this.pos.set(x,y);
        this.region=sprite.region;
    }

    public StaticSprite(String name, Vector2 pos) {
        this(name);
        this.pos.set(pos);
    }

    public StaticSprite(String name, float x, float y) {
        this(name);
        this.pos.set(x, y);
    }
    public int width(){
        return region.getRegionWidth();
    }
    public int height(){
        return region.getRegionHeight();
    }
}