package com.eelengine.engine.robot;

import com.eelengine.engine.GridWorld;
import com.eelengine.engine.Tile;

import java.util.ArrayDeque;

import static bpw.Util.max;

public class LightingEngine {
    public static final int airAttenuation=5;
//    private byte max(byte a, byte b){
//        return a>b?a:b;
//    }
//    private byte max(byte a, byte b, byte c){
//        return max(max(a,b),c);
//    }
//    private byte max(byte a, byte b, byte c,byte d){
//        return max(max(a,b),max(c,d));
//    }
    private static class LData{
        int x,y;

        LData(int x, int y) {
            this.x = x;
            this.y = y;
        }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
    private static int testBro=0;
    public static void runLighting(int x, int y, GridWorld world){
        testBro++;
        // if a square is fully bright, the world has been lit from its position
        if(world.getTile(x,y).visibility==0xff)return;
        ArrayDeque<LData> data=new ArrayDeque<>();
        Tile c,u,d,l,r;
        int vu,vd,vl,vr,old;
        world.getTile(x,y).visibility=0xff;
        data.add(new LData(x+1,y));
        data.add(new LData(x-1,y));
        data.add(new LData(x,y+1));
        data.add(new LData(x,y-1));
        int limit=100000;
        while(!data.isEmpty()){
            if(limit--<0)return;
            LData p=data.pop();
            c=world.getTile(p.x,p.y);
            // skip processed tiles
//            if(c.testBoi==testBro)
//                continue;
            // if a square is fully bright, the world has been lit from its position
            if(c.visibility==0xff)continue;
            u=world.getTile(p.x+1,p.y);
            d=world.getTile(p.x-1,p.y);
            l=world.getTile(p.x,p.y-1);
            r=world.getTile(p.x,p.y+1);
            old=c.visibility;
            vu=u.isSolid()?u.visibility>>1:u.visibility-airAttenuation;
            vd=d.isSolid()?d.visibility>>1:d.visibility-airAttenuation;
            vl=l.isSolid()?l.visibility>>1:l.visibility-airAttenuation;
            vr=r.isSolid()?r.visibility>>1:r.visibility-airAttenuation;
            c.visibility=max(c.visibility, max(max(vu,vd),max(vl,vr)));
            if(old==c.visibility)continue;
            c.testBoi++;
            if(c.visibility>u.visibility)data.add(new LData(p.x+1,p.y));
            if(c.visibility>d.visibility)data.add(new LData(p.x-1,p.y));
            if(c.visibility>l.visibility)data.add(new LData(p.x,p.y-1));
            if(c.visibility>r.visibility)data.add(new LData(p.x,p.y+1));
        }
    }
}
