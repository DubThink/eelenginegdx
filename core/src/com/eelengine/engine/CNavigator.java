package com.eelengine.engine;

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
    Vector2 target=null;
    short targetMode=NONE;
}
