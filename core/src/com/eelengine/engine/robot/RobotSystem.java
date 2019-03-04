package com.eelengine.engine.robot;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.CPhysics;
import com.eelengine.engine.CTransform;
import com.eelengine.engine.FelixLangHelpers;
import com.eelengine.engine.Item;

/**
 * Updates robot logic, processing commands and running scripts
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
        // TODO REMOVE
        if(parts.length>=2&&parts[0].equals("sudo")&&parts[1].equals("kill")){
            Gdx.app.exit();
        } else if(parts.length>=2&&parts[0].equals("move")) {
            Vector2 dir = FelixLangHelpers.ParseDirectionToVec2(parts[1]);
            transform.pos.add(dir);
            if(dir.len2()>0){
                robot.setCooldown(1);
                robot.write("Moving "+dir.toString());
            }
        } else if(parts.length>=2&&(parts[0].equals("inventory")||parts[0].equals("inv"))){
            if(parts.length>=4&&parts[1].equals("insert")){
                try{
                    Item item=Item.valueOf(parts[2]);
                    int amt=Integer.parseInt(parts[3]);
                    amt=robot.inventory.insert(item,amt);
                    robot.write("Inserted "+amt+" "+item.getHrName());
                } catch (NumberFormatException E){
                    robot.writeError("Bad amount");
                }catch (IllegalArgumentException E){
                    robot.writeError("Invalid item");
                }
            } else if(parts[1].equals("list")){
                robot.write("INVENTORY");
                for(Item item:Item.values()){
                    robot.write(String.format("%-12s: %d",
                            item.getHrName(),
                            robot.inventory.getAmount(item)));
                }
            }
        }
        robot.command="";
    }
}