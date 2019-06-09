package com.eelengine.engine.eelgame;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.eelengine.engine.ECS;
import com.eelengine.engine.PHYS;
import com.eelengine.engine.ecs.CPhysics;
import com.eelengine.engine.ecs.CTransform;
import com.eelengine.engine.sprite.AnimatedSpriteInstance;

public class EelEntityBuilder {
    public static int makeTorch(int x, int y){
        return 0;
    }

    public static FixtureDef floorPrint(float x, float y){
        PolygonShape shape = new PolygonShape();
        y-=0.5;
        float verts[] = {
                0,0,
                0,y,
                x,y,
                x,0
        };
        shape.set(verts);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
//        shape.dispose(); // TODO MEMORY LEAK
        return  fixtureDef;
    }

    public static int makePlayer(com.artemis.World entityWorld, World physicsWorld, AnimatedSpriteInstance sprite){
        int ent=entityWorld.create();
        ECS.mSprite.create(ent).setInstance(sprite);
        ECS.mInput.create(ent);
        ECS.mCharacterAnimation.create(ent)
                .setIdleSequence("idle")
                .setMovementSequences("walkup","walkdown","walkside","walkside");
        ECS.mMovement.create(ent).setMaxSpeed(4);
        CTransform cTransform = ECS.mTransform.create(ent);
        cTransform.rotLockedToPhysics=false;
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);

        pc.body.createFixture(floorPrint(1,1));
        pc.body.setFixedRotation(true);
        pc.setFilter(PHYS.PLAYERTEAM,PHYS.ALL);

        return ent;
    }
}
