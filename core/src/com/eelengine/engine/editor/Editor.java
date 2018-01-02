package com.eelengine.engine.editor;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.eelengine.engine.CamController;
import com.eelengine.engine.Etil;
import com.eelengine.engine.FontKit;

import java.util.ArrayList;
import java.util.HashSet;

public class Editor {
    private ArrayList<Brush> brushes=new ArrayList<>();
    private HashSet<Brush> mSelected=new HashSet<>();
    private boolean multiSelected=false;
    private Brush selected=null;
    private int selIdx=-1;
    //Brush altSelected=null;
    private int altSelIdx=-1;
    boolean snapOn=true;
    public int snapLevel=-2;
    float selectThreshold=0.12f;
    private float mouseDownX=0, mouseDownY=0;
    private Vector2 startPos=new Vector2();
    private boolean mouseDown=false;
    public CamController camController;
    private String errormsg="";
    private float errorCooldown=0;

    public Editor(CamController camController) {
        this.camController = camController;
    }

    public void render(PolygonSpriteBatch worldBatch, ShapeRenderer shapeRenderer, SpriteBatch interfaceBatch){
        // -------- RENDER brushes -------- //
        worldBatch.begin();
        for(Brush brush:brushes){
            if(brush==selected)continue;
            brush.render(worldBatch,new Color(.2f,.2f,.0f,.4f));
        }
        worldBatch.end();
        Vector2 wp=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
        snap(wp);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(Brush brush:brushes){
            if(brush==selected)continue;
//            if(brush.isPointIn(camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY())))
//                brush.render(shapeRenderer,Color.GREEN,-1,camController.getZoomFactor());
//            else
                brush.render(shapeRenderer,Color.YELLOW,-1,-1,camController.getZoomFactor());
        }
        shapeRenderer.end();
        if(selected!=null){
            selected.render(worldBatch,new Color(.4f,.0f,.0f,.5f));
            selected.render(shapeRenderer,Color.ORANGE,selIdx,altSelIdx,camController.getZoomFactor());
        }


        if(selected!=null&&mouseDown&&!shiftKey){
            if(selIdx>=0) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.CYAN);
                shapeRenderer.line(new Vector2(selected.getVert(selIdx - 1)).add(selected.pos), wp);
                shapeRenderer.line(new Vector2(selected.getVert(selIdx + 1)).add(selected.pos), wp);
                float screenScale = camController.getZoomFactor() / 30;
                shapeRenderer.rect(wp.x - screenScale, wp.y - screenScale,
                        screenScale * 2, screenScale * 2);
                shapeRenderer.end();
            }else{
                Vector2 wp2=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
                Vector2 dp=new Vector2(wp2.x-startPos.x,wp2.y-startPos.y);
                snap(dp);
                selected.render(shapeRenderer,Color.CYAN,-1,-1,camController.getZoomFactor(),dp);
            }
        }
        // -------- UI -------- //
        interfaceBatch.begin();
        FontKit.SysMedium.setColor(Color.TEAL);
        FontKit.SysMedium.draw(interfaceBatch,"Editor active. Press F9 to toggle.",10, Gdx.graphics.getHeight()-30);
        FontKit.SysMedium.draw(interfaceBatch,
                "Snap "+(!snapOn?"off":"increment="+(snapLevel<0?"1/":"")+(int)Math.pow(2,snapLevel<0?-snapLevel:snapLevel)),
                10, Gdx.graphics.getHeight()-50);
        FontKit.SysMedium.draw(interfaceBatch,"Selected vert #"+selIdx,10, Gdx.graphics.getHeight()-70);
        FontKit.SysMedium.setColor(Color.SCARLET);
        if(!errormsg.isEmpty()&&errorCooldown>0){
            FontKit.SysMedium.draw(interfaceBatch,"Error: "+errormsg,10, 30);
            errorCooldown-=Gdx.graphics.getDeltaTime();
        }
        interfaceBatch.end();

    }
    public void mouseDown(int screenX, int screenY,int button){
        if(button!=0)return; //ignore left clicks
        mouseDownX=screenX;
        mouseDownY=screenY;
        mouseDown=true;
        Vector2 wp=camController.screenToWorld(screenX,screenY);
        startPos.set(wp);
        //snap(wp);
        //check selected brush first

        if(selected!=null) {
            if(shiftKey){
                if(selected.getDistToNearestVert2(wp.x,wp.y)<selectThreshold*camController.getZoomFactor()) {
                    altSelIdx = selected.getNearestVertIdx(wp.x, wp.y);
                    return;
                }
            }
            altSelIdx=-1;
            if(selected.getDistToNearestVert2(wp.x,wp.y)<selectThreshold*camController.getZoomFactor()) {
                selIdx = selected.getNearestVertIdx(wp.x, wp.y);
                return;
            }
        }
        //if that fails, check for a vertex
        for(Brush brush:brushes){
            if(brush.getDistToNearestVert2(wp.x,wp.y)<selectThreshold*camController.getZoomFactor()) {
                selected=brush;
                selIdx = selected.getNearestVertIdx(wp.x, wp.y);
                return;
            }
        }
        // if that fails, check for a Brush
        Brush nearest=null;
        float ndist=1*camController.getZoomFactor();
        for(Brush brush:brushes){
            if(brush.isPointIn(wp)){
                nearest=brush;
                break;
            }
            if(brush.getDistToNearestVert2(wp.x,wp.y)<ndist)
                nearest=brush;
        }
        selIdx=-1;
        altSelIdx=-1;
        selected=nearest;
    }

    public void mouseUp(int screenX, int screenY,int button){
        if(button!=0)return; //ignore left clicks
        mouseDown=false;
        if(Math.abs(mouseDownX-screenX)<10&&Math.abs(mouseDownY-screenY)<10)return;
        Vector2 wp=camController.screenToWorld(screenX,screenY);
        Vector2 dp=new Vector2(wp.x-startPos.x,wp.y-startPos.y);
        snap(dp);
        snap(wp);
        if(selected!=null){
            if(selIdx>=0)selected.setVert(wp.x,wp.y,selIdx);
            else{
                selected.pos.add(dp);
            }
        }
    }
    /** returns false if brush already exists */
    public boolean addBrush(Brush brush){
        if(!brushes.contains(brush)){
            brushes.add(brush);
            return true;
        }else return false;
    }
    public void addBrush(float[] verts){
        brushes.add(new Brush(verts));
    }
    public void addBrush(Vector2 ... verts){
        brushes.add(new Brush(verts));
    }
    public void selectBrushByNum(int num){
        if(num<0||num>=brushes.size())return;
        selected=brushes.get(num);
    }
    void snap(Vector2 v){
        if(snapOn){
            v.set(Util.round(v.x,(float)Math.pow(2,snapLevel)),Util.round(v.y,(float)Math.pow(2,snapLevel)));
        }
    }

    public ArrayList<Body> buildStatics(World world){
        ArrayList<Body> ret=new ArrayList<>();
        for(Brush brush:brushes) {
            BodyDef myBodyDef = new BodyDef();
            myBodyDef.position.set(brush.pos);
            Body body = world.createBody(myBodyDef);
            PolygonShape shape = new PolygonShape();
            shape.set(brush.getFloatArray());
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            body.createFixture(fixtureDef);
            shape.dispose();
            ret.add(body);
        }
        return ret;
    }

    private boolean shiftKey=false;//,ctrlKey=false;
    public void shiftDown(){shiftKey=true;}
    public void shiftUp(){shiftKey=false;}
//    public void ctrlDown(){}
//    public void ctrlUp(){}
    public void error(String errormsg){
        this.errormsg=errormsg;
        this.errorCooldown=6;
    }
//    public void addVert(){
//        if(selected!=null) {
//            if(selected.getCount()>=8) {
//                error("Cannot have a brush with more than 8 verts");
//            }else {
//                Vector2 wp = camController.screenToWorld(Gdx.input.getX(), Gdx.input.getY());
//                snap(wp);
//                selected.addVert(wp);
//            }
//        }
//    }

    public void delete(){
        if(selected!=null){
            if(selIdx>=0){
                if(selected.getCount()<=3) {
                    error("Cannot have a brush with less than 3 verts");
                }else {
                    selected.removeVert(selIdx);
                    selIdx = -1;
                }
            }
            else{
                brushes.remove(selected);
                selected=null;
            }
        }
    }
    public void centerOrigin(){
        if(shiftKey){
            for(Brush brush:brushes)brush.centerOrigin();
        }
        else if(selected!=null)selected.centerOrigin();
    }

    public void split(){
        if(selected!=null&&altSelIdx>=0){
            int a=selIdx;
            int b=altSelIdx;
            if(a>b){//swap so b is greatest
                int tmp=a;
                a=b;
                b=tmp;
            }
            if(!selected.areNeighbors(selIdx,altSelIdx)){
                {
                    brushes.add(new Brush(selected.getFloatArray(),a,b));
                    for(int i=a+1;i<b;i++)selected.removeVert(a+1);
                }
            }else{ // are neighbors, try subdivide
                if(selected.getCount()>=8) {
                    error("Cannot have a brush with more than 8 verts");
                }else {
                    Vector2 half=new Vector2(Util.halfBetween(selected.getVert(a),selected.getVert(b)));
                    selected.addVert(half,a+1);
                }
            }
        }
    }
}
