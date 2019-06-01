package com.eelengine.engine.ecs;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class COneTex extends Component {
    Texture texture=null;
    float offsetX=0.5f;
    float offsetY=0.5f;

    /**
     * sets te offset of the texture as a portion of the texture.
     * e.g. (0.5,0.5) is centered, (0,1) is bottom right corner
     */
    public COneTex setOffset(float x, float y){
        offsetX=x;
        offsetY=y;
        return this;
    }
}
