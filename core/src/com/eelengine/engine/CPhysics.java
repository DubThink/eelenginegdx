package com.eelengine.engine;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Physics component
 *
 * NOTE: {@link #buildBody(World)} must be called to initialize {@link #body}. EelEngine is currently configured with a
 * subscriber to do this automatically in case it is not done beforehand. By default, the physics body will be an empty
 * static * object at (0,0).
 */
public class CPhysics extends Component {
    static final BodyDef initialBodyDef=new BodyDef();
    Body body=null;
    private Filter filter=null;
    /**
     * A number of seconds the CPhysics is "stunned" for. While @<code>stunnedFor!=0</code>,
     *  physics object should not be controlled.
     */
    float stunnedFor=0;

    public boolean isStunned(){
        return  stunnedFor>0;
    }
    public CPhysics() {
    }
    public CPhysics buildBody(World world){
        if(body==null)body=world.createBody(initialBodyDef);
        return this;
    }

    /**
     * Sets internal filter's params. NOTE: all values are casted to shorts. Use consts from {@link PHYS}
     */
    public CPhysics setFilter(int categoryBits, int maskBits){
        if(filter==null)setFilter(categoryBits,maskBits,0);
        else setFilter(categoryBits,maskBits,filter.groupIndex);
        return this;
    }

    /**
     * Sets internal filter's params. NOTE: all values are casted to shorts. Use consts from {@link PHYS}
     */
    public CPhysics setFilter(int categoryBits, int maskBits,int groupIndex){
        if(filter==null)
            filter=new Filter();
        filter.categoryBits=(short)categoryBits;
        filter.maskBits=(short)maskBits;
        filter.groupIndex=(short)groupIndex;
        updateFilter();
        return this;
    }
    public CPhysics setFilterTo(Filter filter){
        this.filter=filter;
        updateFilter();
        return this;
    }
    private void updateFilter(){
        for(Fixture f:body.getFixtureList()){
            f.setFilterData(filter);
        }
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
