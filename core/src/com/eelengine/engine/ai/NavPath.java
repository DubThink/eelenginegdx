package com.eelengine.engine.ai;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.CTransform;
import bpw.Util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Benjamin on 5/27/2017.
 */
public class NavPath {
    Tendril headTendril;
    Navigation navigation;
    CTransform targetTransform=null;
    public float targetX;
    public float targetY;
    LinkedList<Cell> cellChain;
    public NavPath(Navigation navigation,Tendril headTendril, float targetX, float targetY) {
        this.headTendril = headTendril;
        this.navigation = navigation;
        this.targetX = targetX;
        this.targetY = targetY;
        cellChain =new LinkedList<>();
        headTendril.buildCellChain(cellChain);
    }

    public void setTargetTransform(CTransform targetTransform) {
        this.targetTransform = targetTransform;
    }

    /**
     *
     * @param loc the current location of the navigating entity
     * @return false if path is stale
     */
    public boolean putNextTargetVector(Vector2 out,Vector2 loc){
        //Check path valid
        Cell destinationCell;
        if(targetTransform!=null) {
            //System.out.println("setting destNode to cell at targetPawn");
            destinationCell = navigation.getCellAt(targetTransform.pos);
        }else {
            destinationCell = cellChain.get(cellChain.size()-1);
        }
        if(targetTransform!=null&& cellChain.get(cellChain.size()-1)!= destinationCell) {
            out.set(0,0);
            return false;
        }

        //Check if we're in the same cell
        Cell currentCell =navigation.getCellAt(loc.x,loc.y);
//        System.out.println("Current:"+currentCell);
//        System.out.println("Dest:"+destinationCell);
        if(destinationCell.equals(currentCell)){
            //System.out.println("Last cell");
            out.set(getTargetX()-loc.x,getTargetY()-loc.y);
        }else {
            //if we aren't, trim tree and target next cell
            if (currentCell == cellChain.get(0)) cellChain.remove(0);
            out.set(cellChain.get(0).centerX() - loc.x, cellChain.get(0).centerY() - loc.y);
        }
        return true;
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
            renderer.setColor(0,1,0,1);
//            System.out.printf("%.3f %.3f %.3f %.3f\n",cell1.centerX(), cell1.centerY(), cell2.centerX(), cell2.centerY());
            renderer.line(cell1.centerX(), cell1.centerY(), cell2.centerX(), cell2.centerY());
        }
        //renderer.fill(0,255,0,100);
        //System.out.println("To "+getTargetX()+" "+getTargetY());
        // -.1f because gdx draws ellipses by their bottom corner ?!?!?!?!?!?!?
        renderer.ellipse(getTargetX()-.1f,getTargetY()-.1f,.2f,.2f);
        renderer.end();
    }
    public void print(){
        headTendril.print();
    }
}
