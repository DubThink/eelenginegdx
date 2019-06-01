package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.eelengine.engine.EelGame;

public class SpriteInstance {
//    public static final int
    private Sprite sprite;
    public int x;
    public int y;
    protected int idx=0;
    private boolean removed;
//    int facing=0;

    public SpriteInstance(Sprite sprite, int x, int y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Batch batch){
        if(removed)return;
        batch.draw(sprite.regions[idx],x* EelGame.GSCALE,y*EelGame.GSCALE,sprite.regions[idx].getRegionWidth(),sprite.regions[idx].getRegionHeight());
    }

    /**
     * Sets which of the sprite's versions is shown. Wraps if out of bounds
     * @param idx
     */
    public void setIdx(int idx){
        this.idx=idx;
    }

    public Sprite getSprite() {
        return sprite;
    }

    TextureRegion getCurrentRegion(){
        return sprite.getRegion(idx);

    }

    public boolean isRemoved() {
        return removed;
    }
    public void markToRemove(){
        removed=true;
    }
}
