package com.eelengine.engine;

import java.io.Serializable;

class Chunk implements Serializable {
    private static final long serialVersionUID = 1L;
    static final int SIZE = 16;
    Tile tiles[][] = new Tile[SIZE][SIZE];
}
