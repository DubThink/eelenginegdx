package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * A set of render helper functions for debugging and editing
 * @author Benjamin Welsh
 */
public class DebugView {
    /**The number of subdivisions per division*/
    public static int divisions=4;

    /**
     * Renders a grid in a world view region
     * @param sr An already active renderer to render the grid to
     * @param camera the camera view to render the grid for
     * @param quarters whether or not to render quarter-units
     */
    public static void drawGrid(ShapeRenderer sr, OrthographicCamera camera, boolean quarters){
        sr.set(ShapeRenderer.ShapeType.Line);
        float num=EelGame.GSCALE*((quarters&&camera.zoom<1)?1f/divisions:1.0f);
        // unproject does some weird mutator thing where Vector3.Zero causes it to flip out
        Vector3 startPos=camera.unproject(new Vector3(0,0,0));
        Vector3 endPos=camera.unproject(new Vector3(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0));

        for(float i = Util.floor(startPos.x,EelGame.GSCALE); i<=Util.ceil(endPos.x,EelGame.GSCALE);i+=num){
            if(i==0)sr.setColor(0,1,0,.5f);
            else lineColors(sr,i,num);
            sr.line(i,startPos.y,i,endPos.y);
        }
        for(float i = Util.floor(endPos.y,EelGame.GSCALE); i<=Util.ceil(startPos.y,EelGame.GSCALE);i+=num){
            if(i==0)sr.setColor(1,0,0,.5f);
            else lineColors(sr,i,num);
            sr.line(startPos.x,i,endPos.x,i);
        }
    }
    private static void lineColors(ShapeRenderer sr,float i,float c){
        if(i%(divisions*divisions*c)==0)sr.setColor(.6f,.6f,.6f,.5f);//sr.setColor(1,1,.8f,.5f);
        else if(i%(divisions*c)==0)sr.setColor(.2f,.2f,.2f,.5f);
        else /*if(i%(1*c)==0)*/sr.setColor(0.1f,0.1f,0.1f,.5f);
        //else sr.setColor(0.1f,0.1f,0.1f,.5f);
    }
}
