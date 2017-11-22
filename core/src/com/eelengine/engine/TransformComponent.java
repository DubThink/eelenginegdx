package com.eelengine.engine;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {
    Vector2 pos=new Vector2(0,0);
    Vector2 scl=new Vector2(1,1);
    float rot=0;
    boolean rotLockedToPhysics=true;

    @Override
    public String toString() {
        return "(pos:"+pos.toString()+"scl:"+scl.toString()+"rot:"+rot+")";
    }
}
