package com.eelengine.engine;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A null class to copy.
 */
public class CAnim extends Component {
    ArrayList<Texture> textures=new ArrayList<>();
    public int state;
    public float animTime;
    public float speed = .05f;
    public CAnim setTextures(Texture ... textures){
        Collections.addAll(this.textures,textures);
        return this;
    }
}
