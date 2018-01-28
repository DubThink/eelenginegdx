package com.eelengine.engine;

import com.artemis.Component;

import java.util.Collections;
import java.util.HashSet;

/**
 * A null class to copy.
 */
public class CTrigger extends Component {
    public static final int RADIUS=0;
    public static final int AABB=1;
    private HashSet<String> flags=new HashSet<>();
    public float width=0;
    public float height=0;
    public float r=0;
    public int mode=RADIUS;

    public CTrigger setAABB(float width,float height){
        this.width=width;
        this.height=height;
        mode=AABB;
        return this;
    }
    public CTrigger setRadius(float r){
        mode=RADIUS;
        this.r=r;
        return this;
    }

    public CTrigger addFlags(String ... flags){
        Collections.addAll(this.flags,flags);
        return this;
    }

    public CTrigger removeFlags(String ... flags){
        for(String flag:flags)
            this.flags.remove(flag);
        return this;
    }
    public boolean checkFlag(String flag){
        return flags.contains(flag);
    }
}
