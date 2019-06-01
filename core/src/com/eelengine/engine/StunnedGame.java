package com.eelengine.engine;

import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.eelengine.engine.robot.CRobot;
import com.eelengine.engine.robot.LightingEngine;
import com.eelengine.engine.robot.RobotMovementSystem;
import com.eelengine.engine.robot.RobotSystem;

import static bpw.Util.min;

public class StunnedGame extends EelGame {

    int robot;
    FelixTerrainGen terrainGen;
    GridWorld gridWorld;


    // UI
    Table terminalTable,scriptTable;
    Label cmdHistoryUI;
    ProgressBar cmdProgressBar;
    ScrollPane scrollPane;
    TextField cmdTextField;
    StaticNoise2D terrainRenderNoise=new StaticNoise2D(0);
    TextArea scriptArea;

    LoadedTextureRegion terrain,ore;
    LoadedTextureRegion background;
    public StunnedGame() {
    }

    public StunnedGame(String level){
        DEV_draw_grid=false;
    }
    @Override
    public void gameCreate() {
        JamFontKit.initFonts();
        assetSystem.finishLoading();
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
