package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class RobotHelper {
    public static int makeRobot(com.artemis.World entityWorld, World physicsWorld){
        int ent=entityWorld.create();

        CTransform cTransform = ECS.mTransform.create(ent);

        ECS.mInput.create(ent);
//        ECS.mMovement.create(ent).setMaxSpeed(0.7f);
        cTransform.pos.set(5.5f,5.5f);
        ECS.mRobotMovement.create(ent).setDesiredPosition(new Vector2(5.5f,5.5f));
        COneTex cOneTex =ECS.mGraphics.create(ent);
//        Texture[] textures=new Texture[12];
//        for(int i=0;i<12;i++){
//            textures[i]=new Texture(Gdx.files.internal("sprites/character/M_Run_Frame_"+i+".png"));
//        }
//
//        ECS.mAnim.create(ent).setTextures(textures);
        cOneTex.texture=new Texture(Gdx.files.internal("felix.png"));//textures[0];
        //cOneTex.setOffset(0.4f,0.5f);
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

        // robot shit

        ECS.mRobot.create(ent);

        return ent;
    }
}
