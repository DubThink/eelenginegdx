package com.eelengine.engine.editor;

import bpw.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.EelGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A physics-oriented serializable polygon class for maps
 */
public class Brush implements Serializable {
    private static final long serialVersionUID = 1L;

    public Vector2 pos=new Vector2(0,0);
    public boolean hidden=false;

    private ArrayList<Vector2> verts=new ArrayList<>();
    private transient boolean floatArrayFresh=false;
    private transient float[] _floatArray;

    private transient PolygonRegion polygonRegion=null;

//    PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
private static EarClippingTriangulator earClipper=new EarClippingTriangulator();
    private static Texture textureSolid=init();
    private static Texture init(){
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0xDEADBEFF); // DE is red, AD is green and BE is blue.
        pix.fill();
        return new Texture(pix);
    }
//    public Brush(Brush toCopy){
//        this();
//        hidden=toCopy.hidden;
//    }

    public Brush(float[] verts, Vector2 pos){
        this();
        for(int i=0;i<verts.length;i+=2)this.verts.add(new Vector2(verts[i],verts[i+1]));
        this.pos.set(pos);
    }

    public Brush(float[] verts){
        this();
        for(int i=0;i<verts.length;i+=2)this.verts.add(new Vector2(verts[i],verts[i+1]));
    }

    public Brush(float[] verts, int firstvert, int lastvert){
        this();
        if(firstvert<0||firstvert>lastvert||lastvert*2>=verts.length){
            // bad indices
            System.err.println("Bad indices for creating brush from float array\n\tfirst="+
                    firstvert+", last="+lastvert+", size="+verts);
        } else {
            for (int i = firstvert*2; i < (lastvert+1)*2; i += 2) this.verts.add(new Vector2(verts[i], verts[i + 1]));
        }
    }

    public Brush(Vector2 ... verts){
        Collections.addAll(this.verts,verts);
    }

    public void render(PolygonSpriteBatch batch, Color fill){
        boolean renderActive=batch.isDrawing();
        //if(polygonRegion==null){ //TODO make efficient
            polygonRegion=new PolygonRegion(new TextureRegion(textureSolid),
                    getFloatArray(),
                    earClipper.computeTriangles(getFloatArray()).toArray());
            //poly=new PolygonSprite(polygonRegion);
        //}
        if(!renderActive)batch.begin();
//        for(float f:polygonRegion.getVertices())
//            System.out.print(f+", ");
//        System.out.println();
//        for(short f:earClipper.computeTriangles(getFloatArray()).toArray())
//            System.out.print(f+", ");
//        System.out.println();
        batch.setColor(fill);
        batch.draw(polygonRegion,
                pos.x*EelGame.GSCALE,pos.y*EelGame.GSCALE,0,0,
                polygonRegion.getRegion().getRegionWidth(),
                polygonRegion.getRegion().getRegionHeight(),
                EelGame.GSCALE,EelGame.GSCALE,0);
        batch.setColor(1,1,1,1);
        if(!renderActive)batch.end();

    }
    public void render(ShapeRenderer renderer, Color stroke, int sel1,int sel2,float screenScale){
        render(renderer, stroke, sel1,sel2, screenScale,Vector2.Zero);
    }
    public void render(ShapeRenderer renderer, Color stroke, int sel1,int sel2,float screenScale,Vector2 displacement) {
        boolean renderActive=renderer.isDrawing();
        screenScale/=30;
        if(verts.size()<=1)return;
        float[] floats=getFloatArray();
        if(!renderActive)renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.translate(pos.x,pos.y,0);
        renderer.translate(displacement.x,displacement.y,0);
        renderer.setColor(stroke);
        if(verts.size()==2)
            renderer.line(verts.get(0),verts.get(1));
        else renderer.polygon(floats);
        renderer.x(0,0,screenScale*3);
//        if(!renderActive)renderer.end();
//
//        if(!renderActive)renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i=0;i<floats.length;i+=2){
            renderer.setColor((sel1==i/2||sel2==i/2)?Color.RED:stroke);
            renderer.rect(floats[i]-screenScale,floats[i+1]-screenScale,
                    screenScale*2,screenScale*2);
        }
        renderer.identity();
        if(!renderActive)renderer.end();

    }

    public void removeVert(int idx){
        if(idx<0||idx>=verts.size())return;
        floatArrayFresh=false;
        verts.remove(idx);
    }

//    public Brush addVert(float x, float y){
//        floatArrayFresh=false;
//        verts.add(new Vector2(x-pos.x,y-pos.y));
//        return this;
//    }
//
//    public Brush addVert(Vector2 point){
//        floatArrayFresh=false;
//        verts.add(new Vector2(point.x-pos.x,point.y-pos.y));
//        return this;
//    }

    /**
     * Adds a new vertex at idx
     * @return this (for chains)
     */
    public Brush addVert(Vector2 point,int idx){
        floatArrayFresh=false;
        verts.add(idx,new Vector2(point.x-pos.x,point.y-pos.y));
        return this;
    }

    /**
     * sets the position of vertex idx
     * does not do anything on bad idx
     */
    public void setVert(float x, float y, int idx){
        if(idx<0||idx>=verts.size())return;
        floatArrayFresh=false;
        verts.get(idx).set(x-pos.x, y-pos.y);
    }

    /**
     * Returns the index of the vertex closest to the point (x,y).
     * Returns -1 if empty
     */
    public int getNearestVertIdx(float x, float y){
        if(verts.size()==0)return -1;
        x-=pos.x;
        y-=pos.y;
        float dist=Float.MAX_VALUE;
        int idx=0;
        for(int i=0;i<verts.size();i++){
            float tmp= Util.dist2(verts.get(i).x,verts.get(i).y,x,y);
//            System.out.println("Idx: "+i+" "+verts.get(i)+" dist: "+tmp);
            if(tmp<dist){
                dist=tmp;
                idx=i;
            }
        }
        return idx;
    }

    /**
     * Returns the distance to the nearest vertex squared
     */
    public float getDistToNearestVert2(float x, float y){
        x-=pos.x;
        y-=pos.y;
        float dist=Float.MAX_VALUE;
        for(Vector2 vert:verts){
            dist=Util.min(dist,Util.dist2(vert.x,vert.y,x,y));
        }
        return dist;
    }

    /**
     * Returns an array of float values representing the vertices
     * format [x1, y1, x2, y2, ..., xn, yn]
     */
    public float[] getFloatArray(){
        if(!floatArrayFresh) {
            _floatArray = new float[verts.size() * 2];
            for (int i = 0; i < verts.size(); i++) {
                _floatArray[i*2] = verts.get(i).x;
                _floatArray[i*2+1] = verts.get(i).y;
            }
            floatArrayFresh=true;
        }
        return _floatArray;
    }

    /**
     * Returns true if point c is in the brush
     */
    public boolean isPointIn(Vector2 c) {
        c=new Vector2(c).sub(pos);
        int n=0;
        Vector2 d=new Vector2(10000f,10000f);
//        System.out.println("-");
        for(int i=0;i<verts.size();i++){
            boolean intersect=Util.intersect(verts.get(i),verts.get((i+1)%verts.size()),c,d);
            if(intersect)n++;
//            System.out.println(verts.get(i)+" "+verts.get((i+1)%verts.size())+" "+c+" "+d+" "+intersect);
        }
        return n%2==1;
    }

    public Vector2 getVert(int idx){
        return verts.get((idx+verts.size())%verts.size());
    }

    public int getCount() {
        return verts.size();
    }

    /**
     * Moves origin to the center of the brush (calculated as the average of the vertices)
     */
    public void centerOrigin(){
        Vector2 sum=new Vector2();
        for(Vector2 vert:verts){
            sum.add(vert);
        }
        sum.scl(1f/verts.size());
//        System.out.println("new center: "+sum);
        setOrigin(sum);
    }

    /**
     * Sets the origin to a new position without moving brush geometry.
     */
    public void setOrigin(Vector2 origin){
        pos.add(origin);
        for(Vector2 vert:verts){
            vert.sub(origin);
        }
        floatArrayFresh=false;
    }

    /**
     * returns true if indices a and be are neighboring vertices
     */
    public boolean areNeighbors(int a, int b){
//        System.out.println(a+" "+b+" "+(a-b)+" "+(a-b)%verts.size());
//        int c=(a-b+verts.size())%verts.size();
        if(a==0&&b==verts.size())return true;
        if(b==0&&a==verts.size())return true;
        return a-b==-1||a-b==1;
    }

    @Override
    public String toString() {
        String s="Brush[";
        for(Vector2 v:verts){
            s+="("+v.x+","+v.y+"),";
        }
        return s+"]";
    }
}
