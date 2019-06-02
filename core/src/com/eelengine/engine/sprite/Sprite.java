package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.eelengine.engine.EelGame;

import java.util.Collection;

public class Sprite {
    TextureRegion[] regions;
    public float originx;
    public float originy;

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

    public float getWidth(){
        return regions[0].getRegionWidth()/ EelGame.GSCALE_F;
    }
    public float getHeight(){
        return regions[0].getRegionHeight()/ EelGame.GSCALE_F;
    }

    public void setOrigin(float x, float y){
        this.originx=x;
        this.originy=y;
    }
}
