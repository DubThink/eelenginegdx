package com.eelengine.engine.editor;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class MailboxSrc implements Serializable{
    public MailboxSrc(Vector2 location, int zone) {
        this.location = location;
        this.zone = zone;
    }

    public Vector2 location;
    public int zone=1;

}
