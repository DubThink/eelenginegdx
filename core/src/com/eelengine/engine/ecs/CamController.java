package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Encapsulates an orthographic camera and controls zoom and position of the camera
 * @author Benjamin Welsh
 */
public class CamController {
    private OrthographicCamera cam;

    public int ZOOM_MAX=16;
    public int ZOOM_MIN=-10; // zoom in
    public double ZOOM_COEFFICIENT=Math.pow(2,1/3.0);
    private int zoomLevel=0;
    private boolean viewGrabbed;
//    public float viewDisplacementX=0;
//    public float viewDisplacementY=0;

    public CamController(float VIRTUAL_WIDTH,float VIRTUAL_HEIGHT) {
        cam = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        cam.position.x=VIRTUAL_WIDTH/2;
        cam.position.y=VIRTUAL_HEIGHT/2;
        System.out.println(cam.zoom+"   "+cam.position);
    }

    public void updatePan(float dX, float dY){
        if(viewGrabbed){
            cam.translate(dX*getZoomFactor(),dY*getZoomFactor());
        }
    }

    public boolean isViewGrabbed() {
        return viewGrabbed;
    }

    public void setViewGrabbed(boolean viewGrabbed) {
        this.viewGrabbed = viewGrabbed;
    }

    /**
     * Sets the position
     * @param x
     * @param y
     */
    public void setPos(float x, float y){
        cam.position.set(x,y,cam.position.z);
    }

    public void update(){
        cam.zoom=getZoomFactor();
        cam.update();
    }
    public OrthographicCamera getCam() {
        return cam;
    }

    public void setOrtho(float viewportX,float viewportY){
        //cam.setToOrtho(false,viewportX,viewportY);
        cam.up.set(0, 1, 0);
        cam.direction.set(0, 0, -1);
        cam.viewportWidth = viewportX;
        cam.viewportHeight = viewportY;
    }
    public float getZoomFactor(){
        return (float)Math.pow(ZOOM_COEFFICIENT,zoomLevel);
    }

    public float getInvZoomFactor(){
        return 1/getZoomFactor();
    }

    public Vector2 getPos(){
        return new Vector2(cam.position.x,cam.position.y);
    }

    public void setZoomLevel(int i){
        zoomLevel= Util.clamp(i,ZOOM_MIN,ZOOM_MAX);
    }
    public void changeZoomLevel(int i){
        zoomLevel= Util.clamp(zoomLevel+i,ZOOM_MIN,ZOOM_MAX);
    }
    public Vector2 screenToWorld(float screenX, float screenY){
        Vector3 wx=cam.unproject(new Vector3(screenX,screenY,0));
        wx.scl(1/EelGame.GSCALE_F);
        return new Vector2(wx.x,wx.y);
    }

    @Override
    public String toString() {
        return String.format(" zLevel: %d  zFactor: %.3f x:%.3f y:%.3f",zoomLevel,getZoomFactor(),cam.position.x,cam.position.y);
    }
}
