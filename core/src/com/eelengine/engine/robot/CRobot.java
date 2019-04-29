package com.eelengine.engine.robot;

import bpw.Util;
import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.Inventory;

import java.util.LinkedList;

/**
 * A robot.
 *  ..............
 *  .  o      o  .
 * [.      '     .]
 *  .            .
 *  .     ~~     .
 *  ..............
 *  More like the robot's CPU and terminal tbh
 */
public class CRobot extends Component {
    String command="";
    String textBuffer ="";
    LinkedList<String> commandHistory=new LinkedList<>();
    // reversed index: 0 is current command, 1 is the last command in history, 2 is 2 commands ago, etc.
    int historyIndex=0;
    float cooldown =0;
    float cooldownLength=1;
    Inventory inventory=new Inventory(10);
    Script script;
    boolean runningScript=false;

    

    public void startNewScript(String script){
        this.script=new Script(script);
        runningScript=true;
    }
    /**
     * Checks if the robot can accept a command for immediate execution
     * @return true if the robot can accept a command
     */
    public boolean ableToQueueCommand(){
        return command.equals("")&&cooldown==0;
    }

    /**
     * Issues a command to the robot
     * @param command command to run
     */
    public void queueCommand(String command) {
        this.command = command;
        write("> "+command);
        if (commandHistory.size()==0||!commandHistory.peekLast().equals(command))
            commandHistory.addLast(command);
        historyIndex=0;
    }

    /**
     * Writes to the robot's console
     * @param string the message to write
     */
    public void write(String string){
        textBuffer+="\n"+string;
    }

    /**
     * Writes an error message to the robot's console
     * @param string the error message
     */
    public void writeError(String string){
        textBuffer+="\n[#FF8080]"+string+"[]";
    }

    /**
     * Sets the cooldown of the robot's current command
     * @param f the time in seconds
     */
    public void setCooldown(float f){
        cooldown =f;
        cooldownLength=f;
    }

    /**
     * Gets the percent complete the current command's cooldown is
     * @return a percent in [0.0, 1.0]
     */
    public float getCooldownPercent(){
        if(cooldownLength==0)return 1;
        return cooldown /cooldownLength;
    }

    public String getCommand() {
        return command;
    }

    public String previousCommand(){
        historyIndex= Util.clamp(historyIndex+1,0,commandHistory.size());
        if(historyIndex==0)return "";
        return commandHistory.get(commandHistory.size()-historyIndex);
    }

    public String nextCommand(){
        historyIndex= Util.clamp(historyIndex-1,0,commandHistory.size());
        if(historyIndex==0)return "";
        return commandHistory.get(commandHistory.size()-historyIndex);
    }

    /**
     * Gets the robot's console text
     */
    public String getTextBuffer() {
        return textBuffer;
    }
}
