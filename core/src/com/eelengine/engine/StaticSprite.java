package com.eelengine.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Sprite for in-world rendering. Contains transform, so not for use with ECS
 */
public class StaticSprite {

    LoadedTextureRegion region;
    Vector2 pos=new Vector2(0,0);

    public StaticSprite(String name){
        region=new LoadedTextureRegion(name);
    }

    public StaticSprite(String name, Vector2 pos) {
        this(name);
        this.pos = pos;
    }
}
