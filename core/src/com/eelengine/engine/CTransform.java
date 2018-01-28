package com.eelengine.engine;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class CTransform extends Component {
    public Vector2 pos=new Vector2(0,0);
    public Vector2 scl=new Vector2(1,1);
    public float rot=0; // In radians!
    public boolean rotLockedToPhysics=true;

    /**
     * Points the up direction at the velocity
     * Has no effect if {@link #rotLockedToPhysics}
     * see {@link PhysicsToTransformUpdateSystem} for implementation
     */
    boolean pointAtVelocity=false;

    public CTransform setScale(float scl) {
        this.scl.set(scl,scl);
        return this;
    }
    public CTransform setPos(float x, float y) {
        this.pos.set(x,y);
        return this;
    }
    public CTransform setPos(Vector2 pos) {
        this.pos = pos;
        return this;
    }

    public CTransform setRot(float rot) {
        this.rot = rot;
        return this;
    }

    @Override
    public String toString() {
        return "(pos:"+pos.toString()+"scl:"+scl.toString()+"rot:"+rot+")";
    }
}
