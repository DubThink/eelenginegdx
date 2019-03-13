package com.eelengine.engine;

import extlib.OpenSimplexNoise;

import static java.lang.Double.max;

public class FelixTerrainGen {
    OpenSimplexNoise simplexNoise;
    private long seed=0;
    public FelixTerrainGen(long seed) {
        this.seed=seed;
         simplexNoise = new OpenSimplexNoise(seed);

    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        simplexNoise = new OpenSimplexNoise(seed);
    }

    /**
     * Generates chunk at chunk coords u,v
     */
    Chunk GenerateChunk(int u, int v){
        Chunk chunk=new Chunk();
        for(int x=0;x<Chunk.SIZE;x++){
            for(int y=0;y<Chunk.SIZE;y++) {
                double stone=simplexNoise.evals(x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.1);
                double dirt=simplexNoise.evals(100000+x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.1);
                double sand=simplexNoise.evals(200000+x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.1);
                double ore=simplexNoise.evals(300000+x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.1);
                Resource base=stone>max(dirt,sand)?Resource.ROCK:(dirt>sand?Resource.DIRT:Resource.SAND);
                Resource oreR=Resource.DIRT;
                switch (base){
                    case SAND:
                        oreR=Resource.COPPER;
                        break;
                    case DIRT:
                        ore*=0.5;
                    case ROCK:
                        oreR=Resource.IRON;
                }

                chunk.tiles[x][y]=new Tile(base,oreR,(int)max(0,16*ore));
            }
        }
        return chunk;
    }
}
