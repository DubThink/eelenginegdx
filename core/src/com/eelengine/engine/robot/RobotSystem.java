package com.eelengine.engine.robot;

import bpw.Util;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.*;

/**
 * Updates robot logic, processing commands and running scripts
 */
public class RobotSystem extends IteratingSystem {
    ComponentMapper<CTransform> mTransform; // injected automatically.
    ComponentMapper<CRobotMovement> mRobotMovement; // injected automatically.
    ComponentMapper<CRobot> mRobot; // injected automatically.
    public GridWorld gridWorld;
    public SergeiGame game;

    public RobotSystem(GridWorld gridWorld, SergeiGame game) {
        super(Aspect.all(CTransform.class,CRobot.class));
        this.gridWorld=gridWorld;
        this.game=game;
}

    @Override
    protected void process(int e) {
        CTransform transform = mTransform.get(e);
        CRobot robot = mRobot.get(e);
        CRobotMovement movement = ECS.mRobotMovement.get(e);

        if(robot.cooldown>0){
            robot.cooldown-=world.delta;
            robot.cooldown= Util.max(0,robot.cooldown);
            return;
        }
        // parse

        String command;
        if(robot.runningScript){
            command=robot.script.getNext();
            robot.script.advance();
            if(robot.script.finished())robot.runningScript=false;
        } else {
            command=robot.command;
            robot.command="";
        }

        if(command.equals(""))return;

        System.out.println("Running bot command: "+command);

        Vector2 target=new Vector2(1,0);
        target.rotateRad(transform.rot);
        target.add(transform.pos);

        String parts[]=command.split(" ");
        if(parts.length>=1&&parts[0].equals("sudo")){
            game.parseSudoCmd(robot,parts);
        } else if(parts.length>=3&&parts[0].equals("tp")) {
            Vector2 delta = FelixLangHelpers.ParseVector2(parts,1);
            if(delta==null){
                robot.writeError("Bad vector");
            }else{
                transform.pos.set(delta);
                assert movement!=null;
                movement.isMoving=false;
            }
        } else if(parts.length>=2&&parts[0].equals("face")) {
            Vector2 dir = FelixLangHelpers.ParseDirectionToVec2(parts[1]);
            assert movement!=null;
            movement.setDesiredDirection(dir);
        } else if(parts.length>=2&&parts[0].equals("move")) {
            Vector2 dir = FelixLangHelpers.ParseDirectionToVec2(parts[1]);
            Vector2 dest=new Vector2(dir);
            assert movement!=null;
            if(gridWorld.getTile(dest.add(transform.pos)).isSolid()){
                robot.writeError("cannot move: solid block");
            } else if(movement.isMoving){
                robot.writeError("cannot move: already moving");
            }else {
                movement.setDesiredDirection(dir);
                movement.setDesiredPosition(dest);
                //transform.pos.add(dir);
                if (dir.len2() > 0) {
                    robot.setCooldown(.25f);
                    robot.write("Moving " + dir.toString());
                }
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
                    if(robot.inventory.getAmount(item)>0)
                        robot.write(String.format("%-12s: %d",
                                item.getHrName(),
                                robot.inventory.getAmount(item)));
                }
            }
        }else if(parts.length>=2&&parts[0].equals("craft")){
            if(!Recipe.hasRecipe(parts[1])){
                robot.writeError("no recipe with that name");
            }else{
                Recipe r=Recipe.getRecipe(parts[1]);
                if(r.checkInputs(robot.inventory)){
                    // craft
                    r.useInputs(robot.inventory);
                    robot.inventory.insert(r.getOutputs());
                } else {
                    robot.writeError("Not enough inputs");
                }
            }
        }else if(parts.length>=1&&parts[0].equals("scan")){
            Tile tile=gridWorld.getTile(target);
            if(!tile.isSolid())robot.write("empty Tile");
            else{
                //scan
                tile.orescanned=true;
                robot.write(tile.getBaseResource()+" tile ("
                        +(int)(100*tile.getResourceDensity())+"% "+tile.getPrimaryResource()+", "+tile.getSolidity()+" units left)");
            }
        }else if(parts.length>=1&&parts[0].equals("mine")){
            Tile tile=gridWorld.getTile(target);
            if(!tile.isSolid()){
                robot.write("The tile is empty");

            } else {
                Item item = tile.mine();
                robot.write("Mined " + item);
                robot.inventory.insert(item, 1);
                LightingEngine.updateLighting(target,gridWorld);
            }
        }
    }
}