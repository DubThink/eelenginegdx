package com.eelengine.engine.editor;

import com.eelengine.engine.StaticSprite;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelSource implements Serializable {
    private static final long serialVersionUID = 4L;
    public ArrayList<Brush> brushes=new ArrayList<>();
    public ArrayList<StaticSprite> staticSprites=new ArrayList<>();
}
