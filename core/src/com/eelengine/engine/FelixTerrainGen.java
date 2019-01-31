package com.eelengine.engine;

public class FelixTerrainGen {
    /**
     * Generates chunk at chunk coords u,v
     */
    Chunk GenerateChunk(int u, int v){
        Chunk chunk=new Chunk();
        float[][] arid=PerlinNoiseGenerator.generatePerlinNoise(Chunk.SIZE,Chunk.SIZE,1);
        float[][] veg=PerlinNoiseGenerator.generatePerlinNoise(Chunk.SIZE,Chunk.SIZE,1);
        for(int x=0;x<Chunk.SIZE;x++){
            for(int y=0;y<Chunk.SIZE;y++) {
                chunk.tiles[x][y]=new Tile((byte) (255*arid[x][y]),(byte) (255*veg[x][y]));
            }
        }
        return chunk;
    }
}
