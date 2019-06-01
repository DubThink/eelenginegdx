package com.eelengine.engine.editor;

import bpw.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.ecs.CamController;
import com.eelengine.engine.FontKit;

public class BaseEditor {
    LevelSource source=new LevelSource();
    boolean snapOn=true;
    public int snapLevel=-2;
    String errormsg="";
    float errorCooldown=0;
    public CamController camController;

    public BaseEditor(CamController camController) {
        this.camController = camController;
    }

    void snap(Vector2 v){
        if(snapOn){
            v.set(Util.round(v.x,(float)Math.pow(2,snapLevel)),Util.round(v.y,(float)Math.pow(2,snapLevel)));
        }
    }
    void error(String errormsg){
        this.errormsg=errormsg;
        this.errorCooldown=6;
    }

    void renderError(SpriteBatch interfaceBatch){
        if (!errormsg.isEmpty() && errorCooldown > 0) {
            FontKit.SysMedium.draw(interfaceBatch, "Error: " + errormsg, 10, 30);
            errorCooldown -= Gdx.graphics.getDeltaTime();
        }
    }
    public void mouseDown(int screenX, int screenY,int button){}
    public void mouseUp(int screenX, int screenY,int button){}
    public void setSource(LevelSource source){
        if(source!=null){
            this.source=source;
        }
    }
    public LevelSource getSource() {
        return source;
    }
    boolean shiftKey=false;//,ctrlKey=false;
    public void shiftDown(){shiftKey=true;}
    public void shiftUp(){shiftKey=false;}
}
