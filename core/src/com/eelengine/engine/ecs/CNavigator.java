package com.eelengine.engine.ecs;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.ai.NavPath;

public class CNavigator extends Component{
    public static final short NONE=0;
    public static final short POINT=1;
    public static final short ENTITY=2;

    NavPath path;
    @EntityId
    int targetEnt=-1;
    Vector2 targetPoint=null;
    short targetMode=NONE;
    Vector2 desiredMove =new Vector2(0,0);
    public CNavigator setMode(short mode){
        targetMode=mode;
        return this;
    }

    /**
     * NOTE: returns the same Vector2 instance each time
     */
    public Vector2 getDesiredMove(){
        return desiredMove;
    }
}
