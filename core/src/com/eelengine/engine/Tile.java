package com.eelengine.engine;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;
    Resource baseResource;
    Resource primaryResource;
    int primaryCount;
    int baseCount;
    public Tile(Resource baseResource, Resource primaryResource, int primaryCount) {
        this(baseResource,primaryResource,primaryCount,16-primaryCount);
    }

    public Tile(Resource baseResource, Resource primaryResource, int primaryCount, int baseCount) {
        this.baseResource = baseResource;
        this.primaryResource = primaryResource;
        this.primaryCount = primaryCount;
        this.baseCount = baseCount;
        assert baseCount>=0;
        assert primaryCount>=0;
        assert baseCount+primaryCount<=16;
    }

    /**
     * returns i in [0,16] representing how much of a solid block is there (0 for not solid)
     * @return
     */
    public int getSolidity(){
        return baseCount + primaryCount;
    }

    /**
     * returns the percentage that the block is the primary resource
     * @return
     */
    public float getResourceDensity(){
        float f=getSolidity();
        if(f==0)return 0;
        return primaryCount/f;
    }
    /**
     * Checks if the tile is solid
     * @return true if the tile is solid
     */
    public boolean isSolid(){
        return getSolidity()!=0;
    }

    /**
     * Mines the block for one resource if possible
     * @return the Item mined. Returns null if not able to mine
     */
    public Item mine(){
        if(!isSolid())return null;
        if(ThreadLocalRandom.current().nextFloat()<getResourceDensity()){
            primaryCount--;
            assert primaryCount>=0;
            return primaryResource.yields;
        }else{
            baseCount--;
            assert baseCount>=0;
            return baseResource.yields;
        }
    }

    public Resource getBaseResource() {
        return baseResource;
    }

    public Resource getPrimaryResource() {
        return primaryResource;
    }

    public int getPrimaryCount() {
        return primaryCount;
    }

    public int getBaseCount() {
        return baseCount;
    }

    @Override
    public String toString() {
        return "Tile()";
    }
}
