package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.ecs.CPhysics;
import com.eelengine.engine.ecs.CTransform;
import com.eelengine.engine.sprite.*;

import static bpw.Util.in;
import static bpw.Util.min;

public class StunnedGame extends EelGame {

    SpriteInstanceManager spriteInstanceManager;
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
        spriteInstanceManager=new SpriteInstanceManager(entityWorld);
        JamFontKit.initFonts();
        tileSheet=new SpriteSheet("nongit/tiles_dungeon_v1.1.png",20,24);
        instance=new SpriteInstance(tileSheet.makeSprite(6,17,6,17,1,2),0,0);
        spriteInstanceManager.addSpriteInstance(instance);
        AnimatedSprite sprite = tileSheet.makeAnimatedSprite(0,17,4,18,1,2);
        sprite.addSequence("lit",.2f,0,1,2,3);
        sprite.addSequence("unlit",0,4);
        instance2=new AnimatedSpriteInstance(sprite,0,0);
        instance2.playSequence("lit");
        spriteInstanceManager.addSpriteInstance(instance2);
        assetSystem.finishLoading();
        camController.setPos(0,0);
        camController.setZoomLevel(-6);
    }

    @Override
    void addECSSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.addECSSystems(worldConfigurationBuilder);
    }

    @Override
    void setupECS() {
        super.setupECS();
        EntitySubscription subscription = entityWorld.getAspectSubscriptionManager().get(Aspect.all(CSpriteInstances.class));
        subscription.addSubscriptionListener(spriteInstanceManager);
    }


    @Override
    public void logicStep() {
    }

    @Override
    public void renderWorld() {
        super.renderWorld();
        worldBatch.begin();
        spriteInstanceManager.updateAnimations(getdt());
        spriteInstanceManager.render(worldBatch);
//        instance2.render(worldBatch);
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
