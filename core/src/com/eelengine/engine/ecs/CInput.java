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
    public static final int MOVEMENT = UP|DOWN|LEFT|RIGHT;
    // Axis
//    public boolean up=false;
//    public boolean down=false;
//    public boolean left=false;
//    public boolean right=false;
    
    private int _down =0;
    private int _on =0;
    private int _up =0;


    public boolean enabled=true;

    /**
     * Marks a key as pressed
     * @param command the key pressed
     * @return this
     */
    public CInput down(int command){
        _down |=command;
        _on |=command;
        return this;
    }

    /**
     * Marks a key as unpressed
     * @param command the key unpressed
     * @return this
     */
    public CInput up(int command){
        _up |= command;
        _on &= ~command;
        return this;
    }

    /**
     * Checks if a key is pressed
     * @param command
     * @return
     */
    public boolean checkOn(int command){
        return (_on&command)!=0;
    }

    /**
     * Checks if a key is newly pressed
     * @param command
     * @return
     */
    public boolean checkDown(int command){
        return (_down&command)!=0;
    }

    /**
     * Checks if a key is newly unpressed
     * @param command
     * @return
     */
    public boolean checkUp(int command){
        return (_up&command)!=0;
    }

    /**
     * Uses an input
     * @param command
     * @return
     */
    public boolean useDown(int command){
        boolean ret=checkDown(command);
        _down&=~command;
        return ret;
    }

    /**
     * Uses an input
     * @param command
     * @return
     */
    public boolean useUp(int command){
        boolean ret=checkUp(command);
        _up&=~command;
        return ret;
    }

    /**
     * Clears the down/up events
     * @return
     */
    public CInput clear(){
        _down=0;
        _up=0;
        return this;
    }

    /**
     * Clears the currently on inputs (and events)
     * @return
     */
    public CInput clearOn(){
        clear();
        _on=0;
        return this;
    }
}
