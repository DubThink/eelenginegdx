package com.eelengine.engine.ecs;

import com.artemis.Component;

/**
 * CInput
 * Tracks up to 32 inputs.
 * Each input can be on or off. Use {@link #checkDown(int)} and {@link #checkUp(int)} to check if input has been put
 * down and/or up since the last update.
 */
public class CInput extends Component {
    public static final int UP=1;
    public static final int DOWN=1<<1;
    public static final int LEFT=1<<2;
    public static final int RIGHT=1<<3;
    // Axis
//    public boolean up=false;
//    public boolean down=false;
//    public boolean left=false;
//    public boolean right=false;
    
    private int _down =0;
    private int _on =0;
    private int _up =0;


    public boolean enabled=true;

    public CInput down(int command){
        _down |=command;
        _on |=command;
        return this;
    }
    public CInput up(int command){
        _up |= command;
        _on &= ~command;
        return this;
    }
    public boolean checkOn(int command){
        return (_on&command)!=0;
    }
    public boolean checkDown(int command){
        return (_down&command)!=0;
    }
    public boolean checkUp(int command){
        return (_up&command)!=0;
    }

    public boolean useDown(int command){
        boolean ret=checkDown(command);
        _down&=~command;
        return ret;
    }
    public boolean useUp(int command){
        boolean ret=checkUp(command);
        _up&=~command;
        return ret;
    }
    public CInput clear(){
        _down=0;
        _up=0;
        return this;
    }
    public CInput clearOn(){
        _on=0;
        return this;
    }
}
