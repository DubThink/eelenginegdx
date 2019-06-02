package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.EelGame;

public class SpriteInstance {
//    public static final int
    private Sprite sprite;
    public float x;
    public float y;

    protected int idx=0;
    private boolean removed=false;

    public SpriteInstance(Sprite sprite, float x, float y) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Batch batch){
        if(removed)return;
        batch.draw(sprite.getRegion(idx),(x-sprite.originx)* EelGame.GSCALE,(y-sprite.originy)*EelGame.GSCALE,
                sprite.getRegion(idx).getRegionWidth(),sprite.getRegion(idx).getRegionHeight());
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

    public void setPos(Vector2 v){
        x=v.x;
        y=v.y;
    }

    public void setPosCentered(Vector2 v){
        x=v.x-sprite.getWidth()/2;
        y=v.y-sprite.getHeight()/2;
    }
}
