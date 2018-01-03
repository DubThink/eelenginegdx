package com.eelengine.engine.editor;

import com.eelengine.engine.StaticSprite;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelSource implements Serializable {
    public ArrayList<Brush> brushes=new ArrayList<>();
    public StaticSprite sprite=new StaticSprite("sig.png");
}
