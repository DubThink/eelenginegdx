package com.eelengine.engine;

import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.sprite.AnimatedSprite;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;
import com.eelengine.engine.sprite.SpriteInstance;
import com.eelengine.engine.sprite.SpriteSheet;

import static bpw.Util.min;

public class StunnedGame extends EelGame {

    SpriteSheet tileSheet;
    SpriteInstance instance;
    AnimatedSpriteInstance instance2;
    public StunnedGame() {
    }

    public StunnedGame(String level){
        DEV_draw_grid=false;
    }
    @Override
    public void gameCreate() {
        JamFontKit.initFonts();
        tileSheet=new SpriteSheet("nongit/tiles_dungeon_v1.1.png",20,24);
        instance=new SpriteInstance(tileSheet.makeSprite(0,17),0,1);
        AnimatedSprite sprite = tileSheet.makeAnimatedSprite(0,17,4,18,1,2);
        sprite.addSequence("lit",.2f,0,1,2,3);
        sprite.addSequence("unlit",0,4);
        instance2=new AnimatedSpriteInstance(sprite,0,0);
        instance2.playSequence("lit");
        assetSystem.finishLoading();
        camController.setPos(0,0);
        camController.setZoomLevel(-6);
    }

    @Override
    void loadSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.loadSystems(worldConfigurationBuilder);
    }

    @Override
    public void logicStep() {
    }

    @Override
    public void renderWorld() {
        super.renderWorld();
        worldBatch.begin();
//        instance.render(worldBatch);
        instance2.render(worldBatch);
        instance2.update(getdt());
//        Vector2 wCoords=getWorldMouse();
//        TextureRegion region=tileSheet.getRegion((int)wCoords.x,(int)wCoords.y);
//        System.out.println((int)wCoords.x+" "+(int)wCoords.y);
//        if(region!=null)worldBatch.draw(region,0,0);
        worldBatch.end();
    }

    @Override
    public void renderUI() {
        super.renderUI();
    }


    public void renderTextCentered(SpriteBatch batch, BitmapFont font, String text, float x, float y){
        GlyphLayout layout = new GlyphLayout(font,text);
        font.draw(batch,text,x-layout.width/2,y+layout.height/2);
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }

    @Override
    public void mouseDown(Vector2 wp,int button) {
        super.mouseDown(wp,button);
    }

    @Override
    public void gameKeyDown(int keycode) {
//        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
//        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat");
        super.gameKeyDown(keycode);
    }

}
