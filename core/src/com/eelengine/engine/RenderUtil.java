package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class RenderUtil {

    public static void renderSprite(SpriteBatch renderBatch, TextureRegion texture, Vector2 pos, float rot) {
        renderSprite(renderBatch, texture, pos, rot);
    }
    public static void renderSprite(SpriteBatch renderBatch, TextureRegion texture, Vector2 pos, float rot, Vector2 scl) {
        renderBatch.draw(texture,
                pos.x*EelGame.GSCALE-texture.getRegionWidth()*.5f,
                pos.y*EelGame.GSCALE-texture.getRegionHeight()*.5f,
                texture.getRegionWidth()*.5f,
                texture.getRegionHeight()*.5f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                scl.x,
                scl.y,
                rot* Util.RAD_TO_DEG_F,
                false);
    }
    public static void renderSprite(PolygonSpriteBatch renderBatch, TextureRegion texture, float x, float y, float rot){

            renderBatch.draw(texture,
                x*EelGame.GSCALE-texture.getRegionWidth()*.5f,
                y*EelGame.GSCALE-texture.getRegionHeight()*.5f,
                texture.getRegionWidth()*.5f,
                texture.getRegionHeight()*.5f,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                1,
                1,
                rot* Util.RAD_TO_DEG_F,
                false);
    }
    public static void renderSprite(PolygonSpriteBatch renderBatch, TextureRegion texture,
                                    Vector2 pos,Vector2 scl, float rot, float offsetX, float offsetY){
            renderBatch.draw(texture,
                pos.x*EelGame.GSCALE-texture.getRegionWidth()*offsetX,
                pos.y*EelGame.GSCALE-texture.getRegionHeight()*offsetY,
                texture.getRegionWidth()*offsetX,
                texture.getRegionHeight()*offsetY,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                scl.x,
                scl.y,
                rot*Util.RAD_TO_DEG_F,
                false);
//            renderBatch.draw(texture,
//                x*EelGame.GSCALE-texture.getRegionWidth()*.5f,
//                y*EelGame.GSCALE-texture.getRegionHeight()*.5f,
//                texture.getRegionWidth()*.5f,
//                texture.getRegionHeight()*.5f,
//                texture.getRegionWidth(),
//                texture.getRegionHeight(),
//                1,
//                1,
//                rot* Util.RAD_TO_DEG_F,
//                false);
    }

}
