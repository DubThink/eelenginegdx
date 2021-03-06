package com.eelengine.engine.ai;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.security.cert.Certificate;
import java.util.ArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;

/**
 * Created by Benjamin on 6/15/2017.
 */
public class Cell {
    ArrayList<Cell> neighbors=new ArrayList<>();
    int x1,y1,x2,y2;
    int id;
    Tendril tendril=null;

    public Cell(int id, int x1, int y1, int x2, int y2) {
        this.id = id;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    public void draw(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0,1,1,1);
//        renderer.fill(255,70);
        renderer.rect(x1,y1,x2-x1,y2-y1);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1,1,1,.2f);
//        renderer.fill(255,70);
        renderer.rect(x1,y1,x2-x1,y2-y1);
        renderer.end();
    }
//    public void draw(ShapeRenderer renderer,float r, float g, float b){
//        renderer.setColor(r,g,b,1);
////        renderer.fill(255,70);
//        renderer.rect(x1,y1,x2,y2);
//    }
    public void drawLinks(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0,0,1,1);
        for(Cell cell :neighbors){
            renderer.curve(centerX(),centerY(),
                    centerX(),centerY()+.75f,
                    cell.centerX(), cell.centerY()-.75f,
                    cell.centerX(), cell.centerY(),
                    10);
        }
        renderer.end();
    }

    public float distTo(Cell cell) {
        return Util.dist(
                Util.halfBetween(x1, x2), Util.halfBetween(y1, y2),
                Util.halfBetween(cell.x1, cell.x2), Util.halfBetween(cell.y1, cell.y2)
        );
    }

    /**
     * distance squared
     */
    public float distTo2(Cell cell) {
        return Util.dist2(
                Util.halfBetween(x1, x2), Util.halfBetween(y1, y2),
                Util.halfBetween(cell.x1, cell.x2), Util.halfBetween(cell.y1, cell.y2)
        );
    }
    public float distTo(float x, float y){
        return Util.dist(Util.halfBetween(x1,x2), Util.halfBetween(y1,y2),x,y);
    }

    /**
     * distance squared
     */
    public float distTo2(float x, float y){
        return Util.dist2(Util.halfBetween(x1,x2), Util.halfBetween(y1,y2),x,y);
    }
    public float centerX(){
        return Util.halfBetween(x1,x2);
    }
    public float centerY(){
        return Util.halfBetween(y1,y2);
    }
    public String toDataString(){
        String me=id+" "+x1+" "+y1+" "+x2+" "+y2;
        String nids="";
        for(int i=0;i<neighbors.size();i++){
            nids+=" "+neighbors.get(i).id;
        }
        return me+nids;
    }
    public boolean aabb(Cell cell) {
        return Util.aabb(x1,y1,x2,y2, cell.x1, cell.y1, cell.x2, cell.y2);
    }
    public boolean aabbInclusive(Cell cell) {
        return Util.aabbInclusiveNoCorners(x1,y1,x2,y2, cell.x1, cell.y1, cell.x2, cell.y2);
    }
    public boolean aabb(int x1, int y1, int x2, int y2) {
        return Util.aabb(x1,y1,x2,y2,this.x1,this.y1,this.x2,this.y2);
    }
    public boolean pointIn(double x, double y){
        return Util.in(x,x1,x2)&& Util.in(y,y1,y2);
    }
    public void addNeighbor(Cell cell){
        if(!neighbors.contains(cell)) neighbors.add(cell);
    }
    public boolean removeNeighbor(Cell cell){
        return neighbors.remove(cell);
    }
    public boolean insertTendril(Tendril newTendril){
        if(tendril==null){
            tendril=newTendril;
            return true;
        }else{
            if(tendril.getChainLength()>newTendril.getChainLength())tendril.previous=newTendril.previous;
            return false;
        }
    }
    @Override
    public String toString() {
        return String.format("Cell#%d (%d, %d)x(%d, %d)",id,x1,y1,x2,y2);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cell))return false;
        Cell o=(Cell)obj;
        return x1==o.x1&&
                y1==o.y1&&
                x2==o.x2&&
                y2==o.y2;
    }
}
