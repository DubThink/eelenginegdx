package com.eelengine.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        ECS.mMovement.create(ent).setMaxSpeed(4).setVehicular(true);

        cOneTex.texture=carTex;
        cOneTex.setOffset(0.4f,0.5f);
        cTransform.pos.set(2,10);
        cTransform.setScale(1.25f);
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f,.5f);
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
        ECS.mMovement.create(ent).setMaxSpeed(4);

        cOneTex.texture=carTex;
        cOneTex.setOffset(0.4f,0.5f);
        cTransform.pos.set(2,10);
        cTransform.setScale(1.25f);
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f,.5f);
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
}
