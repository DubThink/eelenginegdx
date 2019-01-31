package com.eelengine.engine;

import bpw.Util;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.eelengine.engine.editor.LevelData;
import com.eelengine.engine.editor.LevelIO;
import com.eelengine.engine.editor.MailboxSrc;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;
import static java.lang.Math.round;

public class SergeiGame extends EelGame {

    GridWorld gridWorld;
    public SergeiGame() {
    }

    public SergeiGame(String level){
    }
    @Override
    public void gameCreate() {
        gridWorld=new GridWorld();
        JamFontKit.initFonts();
        assetSystem.finishLoading();
    }

    @Override
    public void logicStep() {
        super.logicStep();
    }

    @Override
    public void render() {
        super.render();
        Pixmap pixmap=new Pixmap((int)GSCALE*Chunk.SIZE,(int)GSCALE*Chunk.SIZE,RGBA8888);
        Chunk chunk=gridWorld.getChunk(0,0);
        pixmap.setColor(0xffffffff);
        pixmap.fill();
//        for(int x=0;x<16;x++){
//            for(int y=0;y<16;y++) {
//                pixmap.drawPixel(x,y, chunk.tiles[x][y].arid<<24|chunk.tiles[x][y].veg<<16|0x00ff);
//            }
//        }
        worldBatch.begin();
        Texture texture=new Texture(pixmap);
        worldBatch.draw(texture,0,0);
        //252, 243, 123
        //66, 51, 27
        //186, 192,96
//        Tile tile;
//        int c=(int)(252-186*(float)tile.arid)<<24|(int)(243-192*(float)tile.arid)<<16|(int)(123-96*(float)tile.arid)<<8|0xff;
//        worldBatch.draw
        worldBatch.end();
    }

    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        float topThird=2*height/3;
        interfaceBatch.begin();


        gridWorld.getTile(getWorldMouse());

        interfaceBatch.end();
        super.renderUI();
    }

    public void renderCentered(SpriteBatch batch, BitmapFont font, String text, float x, float y){
        GlyphLayout layout = new GlyphLayout(font,text);
        font.draw(batch,text,x-layout.width/2,y+layout.height/2);
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }

    @Override
    public void mouseDown(Vector2 wp,int button) {
        super.mouseDown(wp,button);
        Vector3 gridPos=camController.getCam().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
        gridWorld.setTile(round(gridPos.x/10),round(gridPos.y/10));
    }

    @Override
    public void gameKeyDown(int keycode) {
//        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
//        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat"));
        super.gameKeyDown(keycode);
    }
}
