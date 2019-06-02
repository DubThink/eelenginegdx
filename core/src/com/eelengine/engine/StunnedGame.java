package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.EntitySubscription;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.ecs.InputSystem;
import com.eelengine.engine.eelgame.CharacterAnimationSystem;
import com.eelengine.engine.eelgame.EelEntityBuilder;
import com.eelengine.engine.sprite.*;

import static bpw.Util.in;
import static bpw.Util.min;

public class StunnedGame extends EelGame {

    SpriteInstanceManager spriteInstanceManager;
    SpriteSheet tileSheet;
    SpriteInstance instance;
    AnimatedSpriteInstance instance2;

    SpriteSheet heroSheet;
    AnimatedSprite heroSprite;
    AnimatedSpriteInstance hero;

    int playerCharacter;

    public StunnedGame() {
    }

    public StunnedGame(String level){
        DEV_draw_grid=false;
    }
    @Override
    public void gameCreate() {
        spriteInstanceManager=new SpriteInstanceManager(entityWorld);
        EntitySubscription subscription = entityWorld.getAspectSubscriptionManager().get(Aspect.all(CSprite.class));
        subscription.addSubscriptionListener(spriteInstanceManager);

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

        heroSheet=new SpriteSheet("nongit/chara_hero.png",4,11);
        heroSprite=heroSheet.makeAnimatedSprite(0,0,3,10);
        heroSprite.setOrigin(1,1);
        int ii[]={0,1,2,1};
        float it[]={.6f,.08f,.6f,.08f};
        heroSprite.addSequence("idle",it,ii);
        heroSprite.setDefaultSequence("idle");
        ii[0]=4;
        ii[1]=5;
        ii[2]=6;
        ii[3]=5;
        heroSprite.addSequence("action",it,ii);
        heroSprite.addSequence("walkdown",.1f,8,9,10,11);
        heroSprite.addSequence("walkside",.1f,12,13,14,15);
        heroSprite.addSequence("walkup",.1f,16,17,18,19);
        hero=new AnimatedSpriteInstance(heroSprite,0,-1);
        playerCharacter = EelEntityBuilder.makePlayer(entityWorld,physicsWorld,hero);
        playerInput = ECS.mInput.get(playerCharacter);

        camController.setPos(0,0);
        camController.setZoomLevel(-6);
        System.out.println("finishing loading...");
        assetSystem.finishLoading();
        System.out.println("finished");
    }

    @Override
    void addECSSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.addECSSystems(worldConfigurationBuilder);
        worldConfigurationBuilder.with(new CharacterAnimationSystem());
        worldConfigurationBuilder.with(WorldConfigurationBuilder.Priority.HIGH-1,new SpriteSystem());
        worldConfigurationBuilder.with(WorldConfigurationBuilder.Priority.LOWEST,new InputSystem());
    }

    @Override
    void setupECS() {
        super.setupECS();
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
//        if(keycode== Input.Keys.D)
//            hero.playSequence("walkside");
//        if(keycode== Input.Keys.S)
//            hero.playSequence("walkdown");
//        if(keycode== Input.Keys.W)
//            hero.playSequence("walkup");
//        if(keycode== Input.Keys.A)
//            hero.playSequence("idle");
//        if(keycode== Input.Keys.E)
//            hero.playSequence("action");
    }

}
