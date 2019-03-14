package com.eelengine.engine;

import bpw.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Etil {
    /**
     * Returns true if the point is within the specified bounds
     */
    public static boolean inBounds(Vector2 point, float x1, float y1, float x2, float y2){
        return Util.in(point.x,x1,x2)&&Util.in(point.y,y1,y2);
    }
    public static Texture init(int color){
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color); // DE is red, AD is green and BE is blue.
        pix.fill();
        return new Texture(pix);
    }

    /**
     * adjusts saturation by saturationFactor in-place
     * @param c the color to adjust
     * @param saturationFactor the multiplier for the saturation
     * @return the color
     */
    public static Color adjustSaturation(Color c, float saturationFactor){
        float[] hsv={0,0,0};
        c.toHsv(hsv);
        hsv[1]*=saturationFactor;
        c.fromHsv(hsv);
        return c;
    }
}
