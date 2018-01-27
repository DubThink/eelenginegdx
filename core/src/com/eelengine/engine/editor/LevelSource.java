package com.eelengine.engine.editor;

import com.eelengine.engine.StaticSprite;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelSource implements Serializable {
    private static final long serialVersionUID = 5L;
    public ArrayList<Brush> brushes=new ArrayList<>();
    public ArrayList<StaticSprite> staticLayer0 =new ArrayList<>();
    public ArrayList<StaticSprite> staticLayer1=new ArrayList<>();
    public ArrayList<StaticSprite> staticLayer2=new ArrayList<>();
    public ArrayList<StaticSprite> getLayer(int i){
        if(i==0)return staticLayer0;
        else if(i==1)return staticLayer1;
        else if(i==2)return staticLayer2;
        else return null;
    }
}
