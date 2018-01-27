package com.eelengine.engine.editor;


import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.CamController;
import com.eelengine.engine.EelGame;
import com.eelengine.engine.FontKit;
import com.eelengine.engine.StaticSprite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpriteEditor extends BaseEditor {
    private int texPage=0;
    public boolean sidepanel=true;
    int selectedSourceIdx=0;

    float mouseDownX=0;
    float mouseDownY=0;
    boolean dragging=false;

    StaticSprite fade=new StaticSprite("semifade_half_black_left.png");

    ArrayList<SpriteSource> sources=new ArrayList<>();

    public void changeTexPage(int amt){
        texPage=Util.clamp(texPage+amt,0,sources.size()-1);
    }

    public SpriteEditor(CamController camController) {
        super(camController);
        loadAssets();
    }

    public void render(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch) {
        worldBatch.begin();
        float warble=(float)(0.75+.25*Math.sin(0.02*Gdx.graphics.getFrameId()));
        Vector2 dp = camController.screenToWorld(mouseDownX, mouseDownY);
        Vector2 wp=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
        dp.sub(wp);
        dp.scl(-1);
        snap(dp);
        for(StaticSprite sprite:source.staticSprites){
            if(sprite.selected)worldBatch.setColor(warble,warble,1,warble);
            else worldBatch.setColor(1,1,1,1);

            if(dragging&&sprite.selected)worldBatch.draw(sprite.region,(sprite.pos.x+dp.x)* EelGame.GSCALE,(sprite.pos.y+dp.y)* EelGame.GSCALE);
            else worldBatch.draw(sprite.region,sprite.pos.x* EelGame.GSCALE,(sprite.pos.y)* EelGame.GSCALE);
        }
        worldBatch.setColor(1,1,1,1);
        worldBatch.end();
    }
    public void activeRender(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch) {
        // -------- UI -------- //
        interfaceBatch.begin();
        FontKit.SysMedium.setColor(Color.TEAL);
        FontKit.SysMedium.draw(interfaceBatch, "SpriteEditor active. Press F9 to toggle.", Gdx.graphics.getWidth()-320, Gdx.graphics.getHeight() - 30);

        FontKit.SysMedium.draw(interfaceBatch, "TAB to toggle side panel.", Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight() - 50);
        FontKit.SysMedium.draw(interfaceBatch,
                "Snap " + (!snapOn ? "off" : "increment=" + (snapLevel < 0 ? "1/" : "") + (int) Math.pow(2, snapLevel < 0 ? -snapLevel : snapLevel)),
                Gdx.graphics.getWidth()-100, Gdx.graphics.getHeight() - 50);
        FontKit.SysMedium.setColor(Color.SCARLET);
        if (!errormsg.isEmpty() && errorCooldown > 0) {
            FontKit.SysMedium.draw(interfaceBatch, "Error: " + errormsg, 10, 30);
            errorCooldown -= Gdx.graphics.getDeltaTime();
        }
        if(sidepanel) {
            interfaceBatch.draw(fade.region,0,0,192,Gdx.graphics.getHeight());
            int pos=-texPage;
            int idx=0;
            for (SpriteSource spriteSource : sources) {
                drawSpriteSource(idx==selectedSourceIdx,0, pos++*150+20/*why do i do this to me*/, spriteSource, interfaceBatch);
                idx++;
            }
        }
        interfaceBatch.end();
    }

    public void addActiveTextureAtMouse(){
        Vector2 wp=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
        snap(wp);
        float x=wp.x;
        float y=wp.y;
        source.staticSprites.add(new StaticSprite(sources.get(selectedSourceIdx).sprite,x,y));
    }

    @Override
    public void mouseDown(int screenX, int screenY, int button) {
        mouseDownX=screenX;
        mouseDownY=screenY;
        Vector2 wp=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());

        if(screenX<128&&sidepanel) {// on sidebar
            System.out.println(screenY + " " + screenY / 150);
            selectedSourceIdx = texPage + (int) Math.floor((Gdx.graphics.getHeight() - screenY) / 150);
            System.out.println("Selected source #" + selectedSourceIdx);
        }else if(button== Input.Buttons.MIDDLE) {
            addActiveTextureAtMouse();
        }else if(button== Input.Buttons.LEFT) {
            dragging=true;
            for(StaticSprite sprite:source.staticSprites){
                System.out.println(wp+", "+sprite.pos+", "+(sprite.pos.x+sprite.width()/EelGame.GSCALE)+", "+(sprite.pos.x+sprite.height()/EelGame.GSCALE));
                if(Util.in(wp.x,sprite.pos.x,(sprite.pos.x+sprite.width()/EelGame.GSCALE))&&Util.in(wp.y,sprite.pos.y,(sprite.pos.y+sprite.height()/EelGame.GSCALE))) {
                    sprite.pSelected = sprite.selected;
                    sprite.selected = true;
                }else if(!shiftKey)sprite.selected=false;
            }
        }
    }

    public void deselectAll(){
        for(StaticSprite sprite:source.staticSprites)sprite.selected=false;
    }

    public void removeSelected(){
        source.staticSprites.removeIf(sprite -> sprite.selected);
    }
    @Override
    public void mouseUp(int screenX, int screenY, int button) {
        Vector2 wp=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
        if(button==Input.Buttons.LEFT) {
            dragging=false;
            if (Util.dist2(screenX, screenY, mouseDownX, mouseDownY) < 40) {
                for (StaticSprite sprite : source.staticSprites) {
                    if (Util.inBox(wp.x, wp.y, sprite.pos.x, sprite.pos.y, sprite.pos.x + sprite.width() / EelGame.GSCALE, sprite.pos.y + sprite.height() / EelGame.GSCALE))
                        sprite.selected = !sprite.pSelected;
                }
            } else {
                Vector2 dp = camController.screenToWorld(mouseDownX, mouseDownY);
                dp.sub(wp);
                snap(dp);
                dp.scl(-1);
                //dragged
                for (StaticSprite sprite : source.staticSprites) {
                    if(sprite.selected)sprite.pos.add(dp);
                }
            }
        }
    }

    public void loadAssets() {
        try {
            List<String> paths = Files.readAllLines(Gdx.files.internal("loadFile.txt").file().toPath());
            for(String path:paths){
                if(path.startsWith("#"))continue;
                loadFromFolder(path);
            }
        } catch (IOException e) {
            System.err.println("WARNING: unable to load loadFile.txt. Loading from root asset directory");
            loadFromFolder("");
        }
    }
    protected void loadFromFolder(String path){
        Path rootPath=new File(Gdx.files.getLocalStoragePath()).toPath();
        File folder = new File(Gdx.files.getLocalStoragePath()+path);
        System.out.println("## Loading sprite assets from " + folder.toString());
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile() && file.toString().endsWith(".png")) {
                Path relative=rootPath.relativize(file.toPath());
                System.out.println("\tLoading file " + relative.toString());
                sources.add(new SpriteSource(new StaticSprite(relative.toString()), relative.toString()));

            }
        }
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("\tDirectory " + listOfFiles[i].getName());
//            }
    }

    public static void drawSpriteSource(boolean green,int x, int y, SpriteSource spriteSource,SpriteBatch interfaceBatch){
        if(green)FontKit.SysSmall.setColor(Color.GREEN);
        else FontKit.SysSmall.setColor(Color.TEAL);
        interfaceBatch.draw(spriteSource.sprite.region,x,y, Util.min(128,spriteSource.width()),Util.min(128,spriteSource.height()));
        FontKit.SysSmall.draw(interfaceBatch,spriteSource.name+"("+spriteSource.width()+"x"+spriteSource.height()+")", x,y-3);
    }
    static class SpriteSource {
        StaticSprite sprite;
        String name;

        public SpriteSource(StaticSprite sprite, String name) {
            this.sprite = sprite;
            this.name = name;
        }
        public int width(){
            return sprite.region.getRegionWidth();
        }
        public int height(){
            return sprite.region.getRegionHeight();
        }
    }
}
