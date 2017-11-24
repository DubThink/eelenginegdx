package com.eelengine.engine;

import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHandler implements ContactListener {
    com.artemis.World entityWorld;

    public CollisionHandler(World entityWorld) {
        this.entityWorld = entityWorld;
    }

    @Override
    public void beginContact(Contact contact) {
        if (!(contact.getFixtureA().getBody().getUserData() instanceof Integer &&
                contact.getFixtureB().getBody().getUserData() instanceof Integer)) return;
        int aid = (Integer) contact.getFixtureA().getBody().getUserData();
        int bid = (Integer) contact.getFixtureB().getBody().getUserData();
        doShit(contact,aid,bid);
        doShit(contact,bid,aid);
    }
    private void doShit(Contact contact, int a, int b){
        if(ECS.mDamager.has(a)&&ECS.mHealth.has(b)){
            ECS.mHealth.get(b).damage(ECS.mDamager.get(a).amount);
            if(ECS.mProjectile.has(a)&&ECS.mProjectile.get(a).destroyOnHit)entityWorld.delete(a);
        }

    }
        @Override
        public void endContact(Contact contact) {}
        @Override
        public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {}
}
