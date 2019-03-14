package com.eelengine.engine;

import java.util.Random;

public class StaticNoise2D {
    private static int GOOD_PRIME=179;
    private static int GOOD_PRIME2=139;
    private int noise[];
    public StaticNoise2D(long seed){
        Random rand = new Random(seed);
        noise=new int[GOOD_PRIME];
        for(int i=0;i<GOOD_PRIME;i++) {
            noise[i] = rand.nextInt(Integer.MAX_VALUE);
        }
    }

    public int get(int x, int y){
        return noise[(GOOD_PRIME+(x+y*GOOD_PRIME2)%GOOD_PRIME)%GOOD_PRIME];
    }
    public boolean test(int x, int y, int bit){
        return (get(x,y)&bit)>0;
    }
}
