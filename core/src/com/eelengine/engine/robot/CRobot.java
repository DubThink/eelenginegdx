package com.eelengine.engine.robot;

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
    float cooldown =0;
    float cooldownLength=1;
//    String errorMessage="";
    Inventory inventory=new Inventory(10);

    public boolean ableToQueueCommand(){
        return command.equals("");
    }
    public void queueCommand(String command) {
        this.command = command;
        write("> "+command);
        if (commandHistory.size()>0&&!commandHistory.peekLast().equals(command))
            commandHistory.addLast(command);
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

    public String getTextBuffer() {
        return textBuffer;
    }
}
