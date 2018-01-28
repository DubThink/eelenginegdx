package com.eelengine.engine.editor;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class Mailbox implements Serializable{
    Vector2 location;
    transient boolean doneToday=false;
    transient int amt=0;
}
