package com.eelengine.engine.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.LinkedList;

public class SpriteSheet {
    Texture texture;
    int xdivs,ydivs;
    public SpriteSheet(String texturePath, int xdivs, int ydivs){
        this.xdivs=xdivs;
        this.ydivs=ydivs;
        texture = new Texture(Gdx.files.internal(texturePath),true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
    }

    public TextureRegion getRegion(int x, int y) {
        return getRegion(x,y,1,1);
    }
    public TextureRegion getRegion(int x, int y,int w, int h){
        if(x<0||x>=xdivs||y<0||y>=ydivs)return null;
        return new TextureRegion(texture,
                x/((float)xdivs),
                y/((float)ydivs),
                (x+w)/((float)xdivs),
                (y+h)/((float)ydivs));
    }
    /**
     * Makes a one-tile sprite from the sprite sheet
     * @param x the x-coord of the tile
     * @param y the y-coord of the tile
     * @return a new sprite object
     */
    public Sprite makeSprite(int x, int y){
        return new Sprite(getRegion(x,y));
    }
    public Sprite makeSprite(int x1, int y1, int x2, int y2){
        return makeSprite(x1, y1, x2, y2,1,1);
    }

    /**
     * Creates a sprite
     * @param x1 x coord of the start of the region
     * @param y1 y coord of the start of the region
     * @param x2 x coord of the end of the region
     * @param y2 y coord of the end of the region
     * @param w the width of each sprite in tiles
     * @param h the height of each sprite in tiles
     * @return a new sprite
     */
    public Sprite makeSprite(int x1, int y1, int x2, int y2, int w, int h){
        ArrayList<TextureRegion> regions = new ArrayList<>();
        for(int y=y1;y<=y2;y+=h){
            for(int x=x1;x<=x2;x+=w){
                System.out.println("getting sprite "+x+" "+y);
                regions.add(getRegion(x,y,w,h));
            }
        }
        return new Sprite(regions);
    }

    public AnimatedSprite makeAnimatedSprite(int x1, int y1, int x2, int y2){
        return makeAnimatedSprite(x1, y1, x2, y2,1,1);
    }
    public AnimatedSprite makeAnimatedSprite(int x1, int y1, int x2, int y2, int w, int h){
        return new AnimatedSprite(makeSprite(x1, y1, x2, y2, w, h));
    }
}
