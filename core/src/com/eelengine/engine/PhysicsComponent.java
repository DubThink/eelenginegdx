package com.eelengine.engine;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Physics component
 *
 * NOTE: {@link #buildBody(World)} must be called to initialize {@link #body}. EelEngine is currently configured with a
 * subscriber to do this automatically in case it is not done beforehand. By default, the physics body will be an empty
 * static * object at (0,0).
 */
public class PhysicsComponent extends Component {
    static final BodyDef initialBodyDef=new BodyDef();
    Body body=null;
    /**
     * A number of seconds the PhysicsComponent is "stunned" for. While @<code>stunnedFor!=0</code>,
     *  physics object should not be controlled.
     */
    float stunnedFor=0;

    public boolean isStunned(){
        return  stunnedFor>0;
    }
    public PhysicsComponent() {


    }
    public void buildBody(World world){
        if(body==null)body=world.createBody(initialBodyDef);
    }
    Vector2 getPos(){
        return body.getPosition();
    }

    /** eee
     *
     * @return
     */
    float getRot(){
        return body.getAngle();
    }
}
