package com.eelengine.engine.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

/**
 * A sprite with animation sequences
 */
public class AnimatedSprite extends Sprite {
    private TreeMap<String, Sequence> sequences = new TreeMap<>();
    private String defaultSequence = null;
    public AnimatedSprite(TextureRegion... regions) {
        super(regions);
    }

    public AnimatedSprite(Collection<TextureRegion> regions) {
        super(regions);
    }

    public AnimatedSprite(Sprite sprite){
        super(sprite);
    }

    public void addSequence(String name, float[] times, int[] indexes){
        sequences.put(name,new Sequence(indexes.clone(),times.clone()));
    }

    public void addSequence(String name, float time, int ... indexes){
        float[] times = new float[indexes.length];
        for(int i=0;i<times.length;i++){
            times[i]=time;
        }
        sequences.put(name,new Sequence(indexes,times));
    }

    public Sequence getSequence(String id){
        if(id==null)return null;
        return sequences.get(id);
    }

    /**
     * A time of 0 stops on that sprite
     */
    class Sequence{
        int[] sprites;
        float[] times;
        Sequence(int[] sprites, float[] times) {
            if(sprites.length!=times.length)
                throw new RuntimeException("animation must have the same number of sprites and times");
            if(sprites.length==0)
                throw new RuntimeException("animation must have at least one frame");
            this.sprites = sprites;
            this.times = times;
        }

        float getTime(int idx){
            return times[idx%times.length];
        }

        int getSprite(int idx){
            return sprites[idx%sprites.length];
        }

        @Override
        public String toString() {
            return "Sequence{" +
                    "sprites=" + Arrays.toString(sprites) +
                    ", times=" + Arrays.toString(times) +
                    '}';
        }
    }

    public String getDefaultSequence() {
        return defaultSequence;
    }

    public void setDefaultSequence(String defaultSequence) {
        this.defaultSequence = defaultSequence;
    }
}
