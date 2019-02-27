package com.eelengine.engine;

import com.artemis.Component;

/**
 * A robot.
 *  ..............
 *  .  *      *  .
 * [.      '     .]
 *  .            .
 *  .     ~~     .
 *  ..............
 */
public class CRobot extends Component {
    String script="";
    String command="";
    String commandHistory="";
    String executingCommand="";
    float cooldown =0;
    float cooldownLength=1;
    String errorMessage="";

    void setCooldown(float f){
        cooldown =f;
        cooldownLength=f;
    }

    float getCooldownPercent(){
        if(cooldownLength==0)return 1;
        return cooldown /cooldownLength;
    }
}
