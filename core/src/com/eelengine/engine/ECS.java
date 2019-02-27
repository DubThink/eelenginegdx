package com.eelengine.engine;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;

/**
 * Container class for {@link ComponentMapper}s.
 * {@link #initialize(World)} must be called after the entity world is started.
 */
public class ECS {
    public static ComponentMapper<COneTex> mGraphics;
    public static ComponentMapper<CTransform> mTransform;
    public static ComponentMapper<CPhysics> mPhysics;
    public static ComponentMapper<CInput> mInput;
    public static ComponentMapper<CDamager> mDamager;
    public static ComponentMapper<CProjectile> mProjectile;
    public static ComponentMapper<CHealth> mHealth;
    public static ComponentMapper<CTeam> mTeam;
    public static ComponentMapper<CNavigator> mNavigator;
    public static ComponentMapper<CMovement> mMovement;
    public static ComponentMapper<CTrigger> mTrigger;
    public static ComponentMapper<CMailbox> mMailbox;
    public static ComponentMapper<CAnim> mAnim;
    public static ComponentMapper<CRobot> mRobot;

//    public static Archetype Bullet;
    /**
     * Initializes the {@link ComponentMapper}s.
     * @param entityWorld The entity world to
     */
    public static void initialize(World entityWorld){
        mGraphics=entityWorld.getMapper(COneTex.class);
        mTransform =entityWorld.getMapper(CTransform.class);
        mPhysics=entityWorld.getMapper(CPhysics.class);
        mInput=entityWorld.getMapper(CInput.class);
        mDamager=entityWorld.getMapper(CDamager.class);
        mProjectile=entityWorld.getMapper(CProjectile.class);
        mHealth=entityWorld.getMapper(CHealth.class);
        mTeam=entityWorld.getMapper(CTeam.class);
        mNavigator=entityWorld.getMapper(CNavigator.class);
        mMovement=entityWorld.getMapper(CMovement.class);
        mTrigger=entityWorld.getMapper(CTrigger.class);
        mMailbox=entityWorld.getMapper(CMailbox.class);
        mAnim=entityWorld.getMapper(CAnim.class);
        mRobot=entityWorld.getMapper(CRobot.class);
        //        Bullet=new ArchetypeBuilder()
//                .add(CDamager.class)
//                .add(CProjectile.class)
//                .build(entityWorld);
    }
}
