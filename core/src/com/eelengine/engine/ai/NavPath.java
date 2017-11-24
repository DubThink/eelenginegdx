package com.eelengine.engine.ai;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.CTransform;
import bpw.Util;

import java.util.ArrayList;

/**
 * Created by Benjamin on 5/27/2017.
 */
public class NavPath {
    Tendril headTendril;
    Navigation navigation;
    CTransform targetTransform=null;
    float targetX;
    float targetY;
    ArrayList<Cell> cellChain;
    public NavPath(Navigation navigation,Tendril headTendril, float targetX, float targetY) {
        this.headTendril = headTendril;
        this.navigation = navigation;
        this.targetX = targetX;
        this.targetY = targetY;
        cellChain =new ArrayList<>();
        headTendril.buildNodeChain(cellChain);
    }

    public void setTargetTransform(CTransform targetTransform) {
        this.targetTransform = targetTransform;
    }

    public Vector2 getNextTargetVector(float x, float y){
        //Check path valid
        Cell destinationCell;
        if(targetTransform!=null) {
            //System.out.println("setting destNode to cell at targetPawn");
            destinationCell = navigation.getNodeAt(targetTransform.pos);
        }else {
            destinationCell = cellChain.get(cellChain.size()-1);
        }
        if(targetTransform!=null&& cellChain.get(cellChain.size()-1)!= destinationCell) {
            return null;
        }

        //Check if we're in the same cell
        Cell currentCell =navigation.getNodeAt(x,y);
        if(destinationCell == currentCell){
            //System.out.println("Last cell");
            return Util.toPoint(x,y, getTargetX(), getTargetY());
        }
        //if we aren't, trim tree and target next cell
        if(currentCell == cellChain.get(0)) cellChain.remove(currentCell);
        return Util.toPoint(x,y, cellChain.get(0));

    }

    private float getTargetX(){
        if(targetTransform!=null)return targetTransform.pos.x;
        else return targetX;
    }

    private float getTargetY(){
        if(targetTransform!=null)return targetTransform.pos.y;
        else return targetY;
    }

    private void updatePathEnd(){
        //No need to do any of this if there's no target eelengine.physics.Pawn
        //check if target is in end cell
        //if not, check if target is in cell along path
        //if not, check if target is in neighbor of end cell
    }
    public void draw(ShapeRenderer renderer){
        //headTendril.draw(p);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0; i< cellChain.size()-1; i++){
            Cell cell1 = cellChain.get(i);
            Cell cell2 = cellChain.get(i+1);
            renderer.setColor(0,1,0,0);
            renderer.line(cell1.centerX(), cell1.centerY(), cell2.centerX(), cell2.centerY());
        }
        //renderer.fill(0,255,0,100);
        renderer.ellipse(getTargetX(),getTargetY(),20,20);
        renderer.end();
    }
    public void print(){
        headTendril.print();
    }
}
