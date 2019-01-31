package com.eelengine.engine;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

class Chonk implements Serializable {
    private static final long serialVersionUID = 1L;
    Chunk chunks[][] = new Chunk[Chunk.SIZE][Chunk.SIZE];
}
public class GridWorld {
    Chonk chonk;
    FelixTerrainGen terrainGen;
    GridWorld(){
        chonk=new Chonk();
        terrainGen=new FelixTerrainGen();
    }
    Tile getTile(Vector2 v){
        return getTile((int)v.x,(int)v.y);
    }
    Tile getTile(int x, int y){
        if(x<0||x>Chunk.SIZE*Chunk.SIZE||y<0||y>Chunk.SIZE*Chunk.SIZE){
            return new Tile();
        }
        Chunk chunk = chonk.chunks[x/Chunk.SIZE][y/Chunk.SIZE];
        if(chunk!=null){
        }else{
        }
        return new Tile();
    }
    void setTile(int x, int y){
        if(x<0||x>Chunk.SIZE*Chunk.SIZE||y<0||y>Chunk.SIZE*Chunk.SIZE){
            return;
        }
        Chunk chunk = chonk.chunks[x/Chunk.SIZE][y/Chunk.SIZE];
        if(chunk==null) {
            System.out.println("Initializing chunk");
            chonk.chunks[x / Chunk.SIZE][y / Chunk.SIZE] = new Chunk();
            chunk = chonk.chunks[x / Chunk.SIZE][y / Chunk.SIZE];
        }
        chunk.tiles[x%Chunk.SIZE][y%Chunk.SIZE]=new Tile();
    }
    Chunk getChunk(int u,int v){
        if(chonk.chunks[u][v]==null){
            chonk.chunks[u][v]=terrainGen.GenerateChunk(0,0);
        }
        return chonk.chunks[u][v];
    }
}
