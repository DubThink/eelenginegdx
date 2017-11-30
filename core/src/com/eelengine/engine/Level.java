package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class Level {
    ArrayList<Body> statics=new ArrayList<>();
    World physicsWorld;

    public Level(World physicsWorld) {
        this.physicsWorld = physicsWorld;
    }


//    public void addWall(Vector2 )
    /**
     * creates a wall with corners (x1,y1) and (x2,y2)
     */
    public void addWall(float x1, float y1, float x2, float y2) {
        addWall(x1, y1, x2, y2,0);
    }
    /**
     * creates a wall. Rotation is about x1,y1
     */
    public void addWall(float x1, float y1, float x2, float y2, float rot){
        BodyDef myBodyDef=new BodyDef();
        myBodyDef.position.set(x1,y1);
        Body body=physicsWorld.createBody(myBodyDef);
        PolygonShape shape=new PolygonShape();
        shape.setAsBox((x2-x1)/2,(y2-y1)/2, new Vector2(x1-x2,y1-y2).scl(0.5f),rot);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
