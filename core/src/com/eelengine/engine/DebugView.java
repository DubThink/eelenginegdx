package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

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
        float num=((quarters&&camera.zoom<1)?1f/divisions:1.0f);
        // unproject does some weird mutator thing where Vector3.Zero causes it to flip out
        Vector3 startPos=camera.unproject(new Vector3(0,0,0)).scl(1/EelGame.GSCALE);
        Vector3 endPos=camera.unproject(new Vector3(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),0)).scl(1/EelGame.GSCALE);
        //System.out.println(num+" "+startPos+" "+endPos);
        for(float i = Util.floor(startPos.x,1); i<=Util.ceil(endPos.x,1);i+=num){
            if(i==0)sr.setColor(0,1,0,.5f);
            else lineColors(sr,i,num);
            sr.line(i,startPos.y,i,endPos.y);
        }
        for(float i = Util.floor(endPos.y,1); i<=Util.ceil(startPos.y,1);i+=num){
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

    void physicsDebugDrawer(Body body, ShapeRenderer shapeRenderer){
        if(!shapeRenderer.isDrawing()){
            System.err.println("Trying to draw but the shapeRenderer is disabled");
            return;
        }
        Vector2 v1=new Vector2();
        Vector2 v2=new Vector2();
        shapeRenderer.setColor(1,1,0,1);
        shapeRenderer.identity();
        shapeRenderer.translate(body.getPosition().x*EelGame.GSCALE,body.getPosition().y*EelGame.GSCALE,0);
        for(Fixture f:body.getFixtureList()){
//            System.out.println("debugrender "+f.getType()+" "+f.getShape().getType());
            switch(f.getType()){
                case Circle:
                    CircleShape circleShape=(CircleShape)f.getShape();
                    shapeRenderer.circle(circleShape.getPosition().x*EelGame.GSCALE,
                            circleShape.getPosition().y*EelGame.GSCALE,
                            circleShape.getRadius()*EelGame.GSCALE);
                    break;
                case Polygon:
                    PolygonShape polygonShape=(PolygonShape)f.getShape();
                    if(polygonShape.getVertexCount()<2)break;
//                    System.out.println("Drawing poly");
                    for(int i=0;i<polygonShape.getVertexCount()-1;i++){
                        polygonShape.getVertex(i,v1);
                        polygonShape.getVertex(i+1,v2);
                        shapeRenderer.line(v1.x*EelGame.GSCALE,
                                v1.y*EelGame.GSCALE,
                                v2.x*EelGame.GSCALE,
                                v2.y*EelGame.GSCALE);
                    }
                    polygonShape.getVertex(polygonShape.getVertexCount()-1,v1);
                    polygonShape.getVertex(0,v2);
                    shapeRenderer.line(v1.x*EelGame.GSCALE,
                            v1.y*EelGame.GSCALE,
                            v2.x*EelGame.GSCALE,
                            v2.y*EelGame.GSCALE);
                    break;
                //case Edge:

                case Chain:
                    ChainShape chainShape=(ChainShape)f.getShape();
                    if(chainShape.getVertexCount()<2)break;
//                    System.out.println("Drawing poly");
                    for(int i=0;i<chainShape.getVertexCount()-1;i++){
                        chainShape.getVertex(i,v1);
                        chainShape.getVertex(i+1,v2);
                        shapeRenderer.line(v1.x*EelGame.GSCALE,
                                v1.y*EelGame.GSCALE,
                                v2.x*EelGame.GSCALE,
                                v2.y*EelGame.GSCALE);
                    }
                    chainShape.getVertex(chainShape.getVertexCount()-1,v1);
                    chainShape.getVertex(0,v2);
                    shapeRenderer.line(v1.x*EelGame.GSCALE,
                            v1.y*EelGame.GSCALE,
                            v2.x*EelGame.GSCALE,
                            v2.y*EelGame.GSCALE);
            }
        }
        shapeRenderer.identity();
    }
}
