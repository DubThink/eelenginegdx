package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.math.Vector2;

public class Etil {
    /**
     * Returns true if the point is within the specified bounds
     */
    public static boolean inBounds(Vector2 point, float x1, float y1, float x2, float y2){
        return Util.in(point.x,x1,x2)&&Util.in(point.y,y1,y2);
    }
}
