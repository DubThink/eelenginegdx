package com.eelengine.engine;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.eelengine.engine.editor.LevelSource;

public class SpriteEditor {
    LevelSource source=new LevelSource();

    private String errormsg="";
    private float errorCooldown=0;

    public void render(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch) {
        worldBatch.begin();
        for(StaticSprite sprite:source.staticSprites){
            worldBatch.draw(sprite.region,sprite.pos.x,sprite.pos.y);
        }
        worldBatch.end();
    }
    public void activeRender(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch) {
        // -------- UI -------- //
        interfaceBatch.begin();
        FontKit.SysMedium.setColor(Color.TEAL);
        FontKit.SysMedium.draw(interfaceBatch, "SpriteEditor active. Press F9 to toggle.", 10, Gdx.graphics.getHeight() - 30);
        FontKit.SysMedium.setColor(Color.SCARLET);
        if (!errormsg.isEmpty() && errorCooldown > 0) {
            FontKit.SysMedium.draw(interfaceBatch, "Error: " + errormsg, 10, 30);
            errorCooldown -= Gdx.graphics.getDeltaTime();
        }
        interfaceBatch.end();
    }
    public void error(String errormsg){
        this.errormsg=errormsg;
        this.errorCooldown=6;
    }
    public void setSource(LevelSource source){
        if(source!=null){
            this.source=source;
        }
    }
    public LevelSource getSource() {
        return source;
    }
}
