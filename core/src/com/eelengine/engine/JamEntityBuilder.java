package com.eelengine.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class JamEntityBuilder {
    public static int makeCar(com.artemis.World entityWorld, Texture carTex, World physicsWorld){
        int ent=entityWorld.create();

        COneTex cOneTex =ECS.mGraphics.create(ent);
        CTransform cTransform = ECS.mTransform.create(ent);
        ECS.mInput.create(ent);
        ECS.mMovement.create(ent).setMaxSpeed(1).setVehicular(true).setTurningRate(1.4f).setMaxTurningRate(2.2f).setAccel(.4f).setDeccel(1f);
        ECS.mTrigger.create(ent).addFlags("CAR").setRadius(.6f);
        cOneTex.texture=carTex;
        cOneTex.setOffset(0.44f,0.5f);
        cTransform.pos.set(2,10);
        cTransform.setScale(1.25f);
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.6f,.25f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        pc.body.createFixture(fixtureDef);
        pc.body.setAngularDamping(10);

        pc.setFilter(PHYS.PLAYERTEAM,PHYS.ALL);
        // COLLISION TEST GROUND
        //pc.body.
        //
        shape.dispose();
        cTransform.rotLockedToPhysics=false;
        return ent;
    }
    public static int makePlayer(com.artemis.World entityWorld, Texture carTex, World physicsWorld){
        int ent=entityWorld.create();

        COneTex cOneTex =ECS.mGraphics.create(ent);
        CTransform cTransform = ECS.mTransform.create(ent);
        ECS.mInput.create(ent);
        ECS.mMovement.create(ent).setMaxSpeed(0.7f);

        cOneTex.texture=carTex;
        //cOneTex.setOffset(0.4f,0.5f);
        cTransform.pos.set(2,10);
        cTransform.setScale(1.25f);
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.1f,.1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        pc.body.createFixture(fixtureDef);
        pc.body.setAngularDamping(10);
        pc.setFilter(PHYS.PLAYERTEAM,PHYS.ALL);
        // COLLISION TEST GROUND
        //pc.body.
        //
        shape.dispose();
        cTransform.rotLockedToPhysics=false;
        return ent;
    }
    public static int makeMailbox(com.artemis.World entityWorld, Vector2 pos){
        int ent=entityWorld.create();
        ECS.mTransform.create(ent).setPos(pos);
        ECS.mMailbox.create(ent);
        return ent;
    }
}
