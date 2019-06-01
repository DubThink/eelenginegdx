package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Collection;

public class Sprite {
    TextureRegion[] regions;

    public Sprite(TextureRegion region) {
        this.regions = new TextureRegion[1];
        this.regions[0] = region;
    }

    public Sprite(TextureRegion ... regions){
        this.regions=regions;
    }
    public Sprite(Collection<TextureRegion> regions){
        this.regions=regions.toArray(new TextureRegion[0]);
    }

    public Sprite(Sprite sprite){
        this.regions=sprite.regions.clone(); // TODO get rid of this for optimization
    }

    TextureRegion getRegion(int i){
        return regions[i%regions.length];
    }
}
