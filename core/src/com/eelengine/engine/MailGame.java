package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.io.File;

public class MailGame extends EelGame {
    LoadedTextureRegion moneyIcon;
    int car;
    int player;
    boolean playerActive=false;
    String levelToBuild=null;
    public MailGame() {
    }

    public MailGame(String level){
        levelToBuild=level;
    }
    @Override
    public void gameCreate() {
        JamFontKit.initFonts();
        // build level
        //setupEditor();
        assetSystem.finishLoading();
        editor.sourceName=levelToBuild;
        if(Gdx.files.internal(levelToBuild).exists()) {
            editor.loadLevel(editor.sourceName);
            //buildLevelStaticSprites(editor.getLevelSource());
        }else {
            System.err.println("ERROR: Unable to load level "+levelToBuild);
        }

        moneyIcon=new LoadedTextureRegion("sprites/interface/dollas.png");
        car=JamEntityBuilder.makeCar(entityWorld,new Texture(Gdx.files.internal("sprites/objects/car.png"),true),physicsWorld);
        entInput=ECS.mInput.get(car);
        player=JamEntityBuilder.makePlayer(entityWorld,new Texture(Gdx.files.internal("bambito.png"),true),physicsWorld);
    }

    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        interfaceBatch.begin();
        GlyphLayout layout = new GlyphLayout(JamFontKit.SysMonolithic,"GAEM");
        JamFontKit.SysMonolithic.draw(interfaceBatch,"GAEM", Gdx.graphics.getWidth()/2-layout.width/2,Gdx.graphics.getHeight()/2+layout.height/2);
        interfaceBatch.draw(moneyIcon,0,0);

        interfaceBatch.end();
        super.renderUI();
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }

    @Override
    public void gameKeyDown(int keycode) {
        if(keycode==Input.Keys.T){
            playerActive=!playerActive;
            entInput=ECS.mInput.get(playerActive?player:car);
            ECS.mInput.get(car).enabled=!playerActive;
        }
        super.gameKeyDown(keycode);
    }
}
