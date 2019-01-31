package com.eelengine.engine;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;

    public Tile() {}

    public Tile(byte arid, byte veg) {
        this.arid = arid;
        this.veg = veg;
    }

    byte resource = Resource.NONE;
    byte arid=0;
    byte veg=0;
}
