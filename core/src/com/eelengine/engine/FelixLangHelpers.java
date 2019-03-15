package com.eelengine.engine;

import com.badlogic.gdx.math.Vector2;

public class FelixLangHelpers {
    /**
     * up    down  left right
     * u     d     l    r
     * north south west east
     * n     s     w    e
     */
    public static Vector2 ParseDirectionToVec2(String s){
        if(s.equals("up")||s.equals("u")||s.equals("north")||s.equals("n")){
            return new Vector2(0,1);
        } else if(s.equals("down")||s.equals("d")||s.equals("south")||s.equals("s")){
            return new Vector2(0,-1);
        } else if(s.equals("left")||s.equals("l")||s.equals("west")||s.equals("w")){
            return new Vector2(-1,0);
        } else if(s.equals("right")||s.equals("r")||s.equals("east")||s.equals("e")){
            return new Vector2(1,0);
        } else return Vector2.Zero;
    }

    public static Vector2 ParseVector2(String[] s, int offset){
        if(s.length<offset+2)return null;
        try{
            return new Vector2(Float.parseFloat(s[offset]),Float.parseFloat(s[offset+1]));
        }catch (NumberFormatException e){
            return null;
        }
    }
}
