package com.eelengine.engine;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple region renderer
 */
public class RobotSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CPhysics> mPhysics; // injected automatically.
    ComponentMapper<CRobot> mRobot; // injected automatically.

    public RobotSystem() {
        super(Aspect.all(CPhysics.class,CRobot.class));
    }

    @Override
    protected void process(int e) {
        CTransform transform = mTransform.get(e);
        CRobot robot = mRobot.get(e);

        if(robot.cooldown>0){
            robot.cooldown-=world.delta;
            robot.cooldown= Util.max(0,robot.cooldown);
            return;
        }
        // parse
        if(robot.command.equals(""))return;
        System.out.println("Running bot command: "+robot.command);
        String parts[]=robot.command.split(" ");
        if(parts.length>=2&&parts[0].equals("move")){
            Vector2 dir=FelixLangHelpers.ParseDirectionToVec2(parts[1]);
            System.out.println(dir.toString());
            transform.pos.add(dir);
            robot.setCooldown(1);
//            try{
//                amt=Integer.parseInt(parts[1]);
//            }catch (NumberFormatException E) {
//                robot.errorMessage = "Bad Number.";
//            }

        }
//        robot.commandHistory+="\n"+robot.command;
        robot.command="";
    }
}