package com.eelengine.engine;

import extlib.OpenSimplexNoise;

import static java.lang.Double.max;

public class FelixTerrainGen {
    OpenSimplexNoise simplexNoise;
    public FelixTerrainGen() {
        reseed();
    }

    /**
     * reinitializes the random number generator from the current seed, as set by CVars.t_seed
     */
    public void reseed(){
        simplexNoise = new OpenSimplexNoise(SergeiGame.CVars.t_seed);
    }

    private boolean isCave(int u, int v, int x, int y){
        double cave_ridge=simplexNoise.evals(400000+x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.1);
        double cave_cut=simplexNoise.evals(500000+x+u*Chunk.SIZE,y+v*Chunk.SIZE,0.03);

        return Math.abs(cave_ridge)< cave_cut*SergeiGame.CVars.t_cave_height + SergeiGame.CVars.t_cave_trim;//SergeiGame.CVars.t_cave_height;
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

//                isCave &= cave_cut> SergeiGame.CVars.t_cave_trim;
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
                int nc=0;
                if(isCave(u,v,x+1,y))nc++;
                if(isCave(u,v,x-1,y))nc++;
                if(isCave(u,v,x,y+1))nc++;
                if(isCave(u,v,x,y-1))nc++;

                if(isCave(u,v,x,y)&&nc> SergeiGame.CVars.t_cave_cont){
                    chunk.tiles[x][y]=new Tile(base,oreR,0,0);

                } else {
                    chunk.tiles[x][y]=new Tile(base,oreR,(int)max(0,16*ore));
                }
            }
        }
        return chunk;
    }
}
