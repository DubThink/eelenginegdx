package com.eelengine.engine.editor;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eelengine.engine.CamController;
import com.eelengine.engine.Etil;

public class Editor {
    public static final int OFF=0;
    public static final int GEOM =1;
    public static final int SPRITE=2;
    public int mode=OFF;
    public GeomEditor geomEditor;
    public SpriteEditor spriteEditor;
    private Table table;
    VerticalGroup pane;
    private int width=180;
    private int exwidth=200;
    protected LevelSource levelSource;

    public Editor(CamController camController) {
        geomEditor=new GeomEditor(camController);
        spriteEditor=new SpriteEditor(camController);
        levelSource=new LevelSource();
        geomEditor.setSource(levelSource);
        spriteEditor.setSource(levelSource);

    }

    public void buildUI(Table table, Skin skin){
        this.table=table;
        table.top();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(Etil.init(0x2A2A2AAF))));
        Label label=new Label("EDITOR",skin);
        //label.setVisible(false);
        table.add(label).pad(10);
        table.row();
        List<String> list=new List<>(skin);
        list.setItems("Edit","play");


        exwidth=(int)table.getWidth();
        width=exwidth-20;
        System.out.println("Building editor ui with width "+exwidth+"("+width+")");
        table.add(list).fillX().width(Util.min(width,200));
        table.row();

        pane=new VerticalGroup();
        pane.setWidth(exwidth);
        pane.fill();
        ScrollPane scrollPane=new ScrollPane(pane,skin);
        scrollPane.setWidth(exwidth);
        scrollPane.setDebug(true);
        table.add(scrollPane).setActorWidth(exwidth);
        Actor holder=new Actor();
        holder.setWidth(width);
        pane.addActor(holder);
        pane.addActor(new Label("1",skin));
        pane.addActor(new Label("2",skin));
        pane.addActor(new Label("3",skin));
        pane.addActor(new Label("4",skin));
        pane.addActor(new Label("1",skin));
        pane.addActor(new Label("2",skin));
        pane.addActor(new Label("3",skin));
        pane.addActor(new Label("4",skin));
        pane.addActor(new Label("1",skin));
        pane.addActor(new Label("2",skin));
        pane.addActor(new Label("3",skin));
        pane.addActor(new Label("4",skin));
        pane.addActor(new Label("1",skin));
        pane.addActor(new Label("2",skin));
        pane.addActor(new Label("3",skin));
        pane.addActor(new Label("4",skin));
        pane.expand();


    }
    public void render(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch){
        geomEditor.render(worldBatch,shapeRenderer,interfaceBatch);
        spriteEditor.render(worldBatch,shapeRenderer,interfaceBatch);
        if(mode==SPRITE)spriteEditor.activeRender(worldBatch, shapeRenderer, interfaceBatch);
        if(mode==GEOM)geomEditor.activeRender(worldBatch, shapeRenderer, interfaceBatch);
    }
    public void shiftDown(){
        geomEditor.shiftDown();
        spriteEditor.shiftDown();
    }
    public void shiftUp(){
        geomEditor.shiftUp();
        spriteEditor.shiftUp();
    }
    public void mouseDown(int screenX, int screenY,int button){
        if(mode==GEOM)geomEditor.mouseDown(screenX, screenY, button);
        else if(mode==SPRITE)spriteEditor.mouseDown(screenX, screenY, button);
    }
    public void mouseUp(int screenX, int screenY,int button){
        if(mode==GEOM)geomEditor.mouseUp(screenX, screenY, button);
        else if(mode==SPRITE)spriteEditor.mouseUp(screenX, screenY, button);
    }
    public void keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (keycode == Input.Keys.S) {
                LevelIO.saveLevelSource(Gdx.files.internal("testEnv.lvlsrc"), levelSource);
                return;
            } else if (keycode == Input.Keys.O) {
                levelSource = LevelIO.loadLevelSource(Gdx.files.internal("testEnv.lvlsrc"));
                System.out.println("level source:"+levelSource);
                geomEditor.setSource(levelSource);
                spriteEditor.setSource(levelSource);
                System.out.println(levelSource.staticLayer0);
//                StaticSprite sprite = new StaticSprite("test_map.png", 0, 0);
//                geomEditor.getSource().staticLayer0.add(sprite);
                //geomEditor.getSource().staticLayer0.add(new StaticSprite("test_map.png",10,0));
                return;
            }
        }
        if(mode== GEOM)geomKeyDown(keycode);
        if(mode== SPRITE)spriteKeyDown(keycode);

    }
    public void spriteKeyDown(int keycode) {
//        if (keycode == Input.Keys.I) {
//            spriteEditor.loadAssets();
//        }
        if (keycode == Input.Keys.TAB) {
            spriteEditor.sidepanel=!spriteEditor.sidepanel;
        }
        else if (keycode == Input.Keys.A) {
            spriteEditor.deselectAll();
        }
        else if (keycode == Input.Keys.BACKSPACE||keycode == Input.Keys.FORWARD_DEL) {
            spriteEditor.removeSelected();
        }
        else if (keycode == Input.Keys.PAGE_UP) {
            spriteEditor.changeTexPage(1);
        }else if (keycode == Input.Keys.PAGE_DOWN) {
            spriteEditor.changeTexPage(-1);
        }else if (keycode == Input.Keys.NUM_1) {
            spriteEditor.numKey(0);
        }else if (keycode == Input.Keys.NUM_2) {
            spriteEditor.numKey(1);
        }else if (keycode == Input.Keys.NUM_3) {
            spriteEditor.numKey(2);
        }else if (keycode == Input.Keys.R) {
            spriteEditor.rotateSelected();
        }
    }
    public void geomKeyDown(int keycode) {
        if (keycode == Input.Keys.LEFT_BRACKET) {
            geomEditor.snapLevel++;
            spriteEditor.snapLevel++;
        } else if (keycode == Input.Keys.RIGHT_BRACKET) {
            geomEditor.snapLevel--;
            spriteEditor.snapLevel--;
//        }else if(keycode == Input.Keys.F8){
//            if(tempWorld!=null){
//                for(Body body:tempWorld)physicsWorld.destroyBody(body);
//            }
//            tempWorld= geomEditor.buildStatics(physicsWorld);
        } else if (keycode == Input.Keys.BACKSPACE || keycode == Input.Keys.FORWARD_DEL || keycode == Input.Keys.X) {
            geomEditor.delete();
//            }else if(keycode == Input.Keys.A){
//                geomEditor.addVert();
        } else if (keycode == Input.Keys.O) {
            geomEditor.centerOrigin();
        } else if (keycode == Input.Keys.S) {
            geomEditor.split();
        } else if (keycode == Input.Keys.D) {
            for (Brush brush : geomEditor.getSource().brushes) {
                System.out.println(brush);
            }
        } else if (keycode == Input.Keys.A) {
            geomEditor.selectAll();
        }else if (keycode == Input.Keys.N) {
            float x=Gdx.input.getX();
            float y=Gdx.input.getY();
            geomEditor.addRectBrushAtMouse(1,1);
        }
    }
}
