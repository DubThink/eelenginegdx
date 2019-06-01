package com.eelengine.engine.ecs;

import com.eelengine.engine.Tile;

import java.io.Serializable;

class Chunk implements Serializable {
    private static final long serialVersionUID = 1L;
    static final int BSIZE = 4;
    static final int SIZE = 1<<4;
    Tile tiles[][] = new Tile[SIZE][SIZE];
}
