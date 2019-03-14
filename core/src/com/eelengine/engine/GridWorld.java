package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/* big chonkus */
class Chonk implements Serializable {
    private static final long serialVersionUID = 1L;
    Chunk chunks[][] = new Chunk[Chunk.SIZE][Chunk.SIZE];
}
public class GridWorld {
    private static final int CENTER=63;
    private static final int WORLDSIZE=127;
    private Chunk[][] chunks = new Chunk[WORLDSIZE][WORLDSIZE];
    FelixTerrainGen terrainGen;
    GridWorld(FelixTerrainGen terrainGen){
        this.terrainGen=terrainGen;
    }
    public Tile getTile(Vector2 v){
        return getTile((int)v.x,(int)v.y);
    }

    public Tile getTile(int x, int y){
        Chunk chunk = getChunk(x>>Chunk.BSIZE,y>>Chunk.BSIZE);
        if(chunk==null)return null;
        return chunk.tiles[(Chunk.SIZE+x%Chunk.SIZE)%Chunk.SIZE][(Chunk.SIZE+y%Chunk.SIZE)%Chunk.SIZE];
    }
    /** returns false if unable to set tile (i.e. oob) */
    boolean setTile(int x, int y, Tile tile){
        Chunk chunk = getChunk(x>>Chunk.BSIZE,y>>Chunk.BSIZE);
        if(chunk==null) return false;
        chunk.tiles[(Chunk.SIZE+x%Chunk.SIZE)%Chunk.SIZE][(Chunk.SIZE+y%Chunk.SIZE)%Chunk.SIZE]=tile;
        return true;
    }

    /**
     * Returns null if chunk outside world
     */
    Chunk getChunk(int u,int v){
        u+=CENTER;
        v+=CENTER;
        if(!Util.inBox(u,v,0,0,WORLDSIZE,WORLDSIZE)){
            return null;
        }
        if(chunks[u][v]==null){
            chunks[u][v]=terrainGen.GenerateChunk(u,v);
        }
        return chunks[u][v];
    }
}
