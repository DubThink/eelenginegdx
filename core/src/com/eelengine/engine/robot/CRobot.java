package com.eelengine.engine.robot;

import bpw.Util;
import com.artemis.Component;
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

    public boolean ableToQueueCommand(){
        return command.equals("");
    }
    public void queueCommand(String command) {
        this.command = command;
        write("> "+command);
        if (commandHistory.size()==0||!commandHistory.peekLast().equals(command))
            commandHistory.addLast(command);
        historyIndex=0;
    }
    public void write(String string){
        textBuffer+=string+"\n";
    }
    public void setCooldown(float f){
        cooldown =f;
        cooldownLength=f;
    }

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
    
    public String getTextBuffer() {
        return textBuffer;
    }
}
