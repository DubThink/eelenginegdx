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
        int teamA=ECS.mTeam.has(a)?ECS.mTeam.get(a).team:0;
        int teamB=ECS.mTeam.has(b)?ECS.mTeam.get(b).team:0;
        boolean diffTeams = !(teamA==teamB&&teamA!=0);
        //System.out.println("Collision between "+ECS.mTeam.has(a)+":"+teamA+" and "+ECS.mTeam.has(b)+":"+teamB);
        if(diffTeams&&ECS.mDamager.has(a)&&ECS.mHealth.has(b)){
            ECS.mHealth.get(b).addDamage(ECS.mDamager.get(a).amount,ECS.mDamager.get(a).type,a);
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
