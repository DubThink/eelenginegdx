package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.eelengine.engine.ai.Navigation;

/**
 * Updates the physics system based on transform.
 */
public class NavigationSystem extends IteratingSystem {
    ComponentMapper<CNavigator> mNavigator; // injected automatically.
    ComponentMapper<CTransform> mTransform; // injected automatically.
//    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    ShapeRenderer renderer;
    Navigation navigation;
    public NavigationSystem( Navigation navigation, ShapeRenderer renderer) {
        super(Aspect.all(CNavigator.class,CTransform.class));
        if(navigation==null)throw new NullPointerException("navigation must not be null");
        this.navigation=navigation;
        this.renderer=renderer;
    }

    @Override
    protected void process(int e) {
        CNavigator navigator=mNavigator.get(e);
        CTransform transform=mTransform.get(e);
        //System.out.println(navigator.targetMode+" "+navigator.targetPoint);
        if(navigator.targetMode==CNavigator.NONE){
            // -------- Mode NONE -------- //
            navigator.desiredMove.set(0,0);
        } else if(navigator.targetMode==CNavigator.POINT&&navigator.targetPoint!=null){
            // -------- Mode POINT -------- //
            if(navigator.path!=null&&
                    navigator.path.targetX==navigator.targetPoint.x&& //the path is pointing at the targetPoint,
                    navigator.path.targetY==navigator.targetPoint.y){ //i.e. the targetPoint has not been updated
                // Path is good
            } else {
                navigator.path = navigation.findPath(transform.pos,navigator.targetPoint);
            }
        }

        // finished updating path, update desiredMove:
        if(navigator.path!=null)navigator.path.putNextTargetVector(navigator.desiredMove,transform.pos);
        else navigator.desiredMove.set(0,0);

        // show debugging information

        //render next dir
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        if (navigator.desiredMove.len2()!=0) {
//            System.out.println("next vector:" + navigator.desiredMove);
            renderer.line(transform.pos.x,transform.pos.y,
                    transform.pos.x+navigator.desiredMove.x,
                    transform.pos.y+navigator.desiredMove.y);
        } else {
            renderer.circle(transform.pos.x, transform.pos.y, .2f);
        }
        renderer.end();

        // render the path
        if(navigator.path!=null){
            navigator.path.draw(renderer);
        }

    }
}