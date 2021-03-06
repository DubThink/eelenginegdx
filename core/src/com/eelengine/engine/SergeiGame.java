package com.eelengine.engine;

import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.eelengine.engine.robot.CRobot;
import com.eelengine.engine.robot.LightingEngine;
import com.eelengine.engine.robot.RobotMovementSystem;
import com.eelengine.engine.robot.RobotSystem;
import javafx.scene.effect.Light;

import static bpw.Util.min;
import static java.lang.Math.round;

public class SergeiGame extends EelGame {

    int robot;
    FelixTerrainGen terrainGen;
    GridWorld gridWorld;


    // UI
    Table terminalTable,scriptTable;
    Label cmdHistoryUI;
    ProgressBar cmdProgressBar;
    ScrollPane scrollPane;
    TextField cmdTextField;
    StaticNoise2D terrainRenderNoise=new StaticNoise2D(0);
    TextArea scriptArea;

    LoadedTextureRegion terrain,ore;
    LoadedTextureRegion background;
    public SergeiGame() {
    }

    public SergeiGame(String level){
        DEV_draw_grid=false;
    }
    @Override
    public void gameCreate() {

        terrainGen=new FelixTerrainGen();
        initializeTerrain();

        Recipe.loadRecipes(Gdx.files.internal("recipes.json"));

        JamFontKit.initFonts();
        assetSystem.finishLoading();
        robot=RobotHelper.makeRobot(entityWorld,physicsWorld);
        playerInput =ECS.mInput.get(robot);

        // SET UP COMMAND LINE
        cmdTextField = new TextField("",skin,"default");
//        cmdTextField.setWidth(200);
//        cmdTextField.setHeight(50);
        cmdTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return 32<=c&&c<=126;
            }
        });
        cmdTextField.setBlinkTime(0.2f);
        cmdTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')&&ECS.mRobot.get(robot).ableToQueueCommand()){
                    System.out.println("executing command");
                    ECS.mRobot.get(robot).queueCommand(textField.getText());
                    textField.setText("");
                }
            }
        });

        stage.setDebugAll(true);
        terminalTable=new Table(skin);
//        terminalTable.setFillParent(true);
        terminalTable.setSize(600,800);
        terminalTable.background("window");
        terminalTable.align(Align.bottom);
        stage.addActor(terminalTable);

        cmdHistoryUI =new Label("",skin);
        scrollPane=new ScrollPane(cmdHistoryUI,skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setScrollingDisabled(true,false);
        cmdProgressBar =new ProgressBar(0,1,.01f,false,skin);
        cmdProgressBar.setValue(.6f);

        terminalTable.add(scrollPane).prefWidth(11111);
        terminalTable.row();
        terminalTable.add(cmdProgressBar).prefWidth(11111);
        terminalTable.row();
        terminalTable.add(cmdTextField).prefWidth(11111);

        // script editor

        scriptTable=new Table(skin);
        scriptTable.setSize(600,800);
        scriptTable.background("window");
        scriptTable.align(Align.bottom);
        scriptTable.setVisible(false);
        stage.addActor(scriptTable);

        scriptArea = new TextArea("hello",skin,"default");
//        area.setTextFieldFilter(new TextField.TextFieldFilter() {
//            @Override
//            public boolean acceptChar(TextField textField, char c) {
//                return 32<=c&&c<=126;
//            }
//        });
        scriptArea.setBlinkTime(0.2f);
        scriptTable.add(scriptArea).prefWidth(11111).prefHeight(11111);
        scriptArea.setFocusTraversal(false);

        terrain=new LoadedTextureRegion("rok2.png");
        ore=new LoadedTextureRegion("ore.png");
        background=new LoadedTextureRegion("bg.png");

    }

    void initializeTerrain(){
        gridWorld=new GridWorld(terrainGen);
        // TODO make less hacky
        entityWorld.getSystem(RobotSystem.class).gridWorld=gridWorld;
    }

    @Override
    void loadSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.loadSystems(worldConfigurationBuilder);
        worldConfigurationBuilder.with(robotSystem=new RobotSystem(gridWorld,this));
        worldConfigurationBuilder.with(new RobotMovementSystem(gridWorld,this));

    }

    @Override
    public void logicStep() {
        CRobot cRobot=null;
        if(robot!=-1)cRobot=ECS.mRobot.get(robot);
//        terminalTable.setVisible(cRobot!=null);
        if(cRobot!=null) {
            // TODO add listener and use scroll events to turn on/off snap-to-bottom
            // TODO or think of an elegant solution (better idea)
            float scrollpos=scrollPane.isScrollY()?1:scrollPane.getScrollPercentY();
            cmdHistoryUI.setText(cRobot.getTextBuffer());
            if(scrollpos==1)scrollPane.setScrollPercentY(1);
            cmdProgressBar.setValue(1-cRobot.getCooldownPercent());

        }
        // Update controls

    }

    @Override
    public void renderWorld() {
        super.renderWorld();

        if(robot>=0){
            CTransform transform=ECS.mTransform.get(robot);
            LightingEngine.runLighting(transform.pos,gridWorld);
        }
        worldBatch.begin();

        Vector2 topleft=camController.screenToWorld(0,0);
        int BGSCALE=64;
        Vector2 bottomright=camController.screenToWorld(viewport.getScreenWidth(),viewport.getScreenHeight());
        for (int u = (int)Math.floor(topleft.x/BGSCALE); u < (int)Math.ceil(bottomright.x/BGSCALE); u++) {
            for (int v = (int)Math.floor(bottomright.y/BGSCALE); v < (int)Math.ceil(topleft.y/BGSCALE); v++) {
                worldBatch.draw(background,BGSCALE*GSCALE*u,BGSCALE*GSCALE*v,BGSCALE*GSCALE,BGSCALE*GSCALE);
            }
        }
        for (int u = (int)Math.floor(topleft.x/Chunk.SIZE); u < (int)Math.ceil(bottomright.x/Chunk.SIZE); u++) {
            for (int v = (int)Math.floor(bottomright.y/Chunk.SIZE); v < (int)Math.ceil(topleft.y/Chunk.SIZE); v++) {
                renderChunk(u, v);
            }
        }

        worldBatch.setColor(Color.WHITE);
        worldBatch.end();
    }

    protected void renderChunk(int u, int v){
        Chunk chunk=gridWorld.getChunk(u,v);
        Color c=Color.RED;
        for(int x=0;x<16;x++){
            for(int y=0;y<16;y++) {
                switch (chunk.tiles[x][y].baseResource) {
                    case SAND:
                        c.set(Color.GOLDENROD);
                        break;
                    case ROCK:
                        c.set(Color.GRAY);
                        break;
                    case DIRT:
                        c.set(Color.BROWN);
                        break;
                    default:
                        c.set(Color.RED);
                }
                if(!chunk.tiles[x][y].isSolid()) {
                    c.r *= .3;
                    c.g *= .3;
                    c.b *= .3;
                    Etil.adjustSaturation(c,0.3f);
                }
                c.a=chunk.tiles[x][y].visibility/256.f;
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                    c.set(Color.BLACK);
                    c.g=min(1,chunk.tiles[x][y].testBoi/8f);
                    c.a=1;
//                    c.b=min(0,chunk.tiles[x][y].testBoi/8);
                }
                worldBatch.setColor(c);
                int ax=x + u*Chunk.SIZE;
                int ay=y + v*Chunk.SIZE;
                    worldBatch.draw(terrain.getTexture(),
                        ax*GSCALE,ay*GSCALE,
                        GSCALE*(terrainRenderNoise.test(ax,ay,1)?0:1),GSCALE*(terrainRenderNoise.test(ax,ay,2)?0:1),
                        GSCALE, GSCALE);
            }
        }

        // draw ores
        for(int x=0;x<16;x++){
            for(int y=0;y<16;y++) {
                int p=chunk.tiles[x][y].getPrimaryCount();
                if(p>0&&chunk.tiles[x][y].orescanned) {
                    if (chunk.tiles[x][y].getPrimaryResource() == Resource.COPPER) {
                        worldBatch.setColor(Color.BROWN);
                    } else {
                        worldBatch.setColor(Color.DARK_GRAY);
                    }
                    worldBatch.draw(ore.getTexture(), (u*Chunk.SIZE+x)*GSCALE,(v*Chunk.SIZE+y)*GSCALE, GSCALE*(p<8?0:1),GSCALE*(p%8<4?0:1), GSCALE, GSCALE);
                }
            }
        }
    }

    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        float topThird=2*height/3;
        interfaceBatch.begin();

        interfaceBatch.end();
        super.renderUI();
    }

    public void renderCentered(SpriteBatch batch, BitmapFont font, String text, float x, float y){
        GlyphLayout layout = new GlyphLayout(font,text);
        font.draw(batch,text,x-layout.width/2,y+layout.height/2);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(cmdTextField==stage.getKeyboardFocus()){
            // handle special keys on the command line
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)&&keycode==Input.Keys.U){
                cmdTextField.setText(cmdTextField.getText().substring(cmdTextField.getCursorPosition()));
            }else if(ECS.mRobot.get(robot)!=null&&ECS.mRobot.get(robot).ableToQueueCommand()){
                if(keycode==Input.Keys.UP){
                    cmdTextField.setText(ECS.mRobot.get(robot).previousCommand());
                    cmdTextField.setCursorPosition(10000);
                }else if(keycode==Input.Keys.DOWN){
                    cmdTextField.setText(ECS.mRobot.get(robot).nextCommand());
                    cmdTextField.setCursorPosition(10000);
                }
            }
        }
        if(keycode == Input.Keys.F1){
            // switch to cmd line
            System.out.println("Switching to cmd");
            terminalTable.setVisible(true);
            stage.setKeyboardFocus(cmdTextField);
            scriptTable.setVisible(false);
        }else if(keycode == Input.Keys.F2){
            // switch to script editor
            System.out.println("Switching to script");
            terminalTable.setVisible(false);
            scriptTable.setVisible(true);
            stage.setKeyboardFocus(scriptArea);
        }
        if(keycode == Input.Keys.NUMPAD_0){
            System.out.println("run script");
            if(robot>=0)ECS.mRobot.get(robot).startNewScript(scriptArea.getText());
        }else if(keycode==Input.Keys.TAB){
            // TODO holy shit very hacky bad
            System.out.println("AAA");
            robot = -1 - robot;
        }
        return super.keyDown(keycode);
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }

    @Override
    public void mouseDown(Vector2 wp,int button) {
        super.mouseDown(wp,button);
//        Vector3 gridPos=camController.getCam().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
//        gridWorld.getTile(round(gridPos.x/10),round(gridPos.y/10));
    }

    @Override
    public void gameKeyDown(int keycode) {
//        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
//        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat");
        super.gameKeyDown(keycode);
    }

    public void parseSudoCmd(CRobot console, String[] parts){
        if(parts.length<2||!parts[0].equals("sudo"))return;
        if(parts[1].equals("quit")){
            Gdx.app.exit();
            return;
        }
        if(parts[1].equals("regenerate")||parts[1].equals("regen")){
            initializeTerrain();
            console.write("Regenerating world...");
            return;
        }
        if(parts[1].startsWith("t_")){
            if(parts.length>=3){
                // set seed
                try {
                    switch (parts[1]){
                        case "t_seed":
                            CVars.t_seed=Long.parseLong(parts[2]);
                            terrainGen.reseed();
                            break;
                        case "t_cave_height":
                            CVars.t_cave_height=Float.parseFloat(parts[2]);
                            break;
                        case "t_cave_trim":
                            CVars.t_cave_trim=Float.parseFloat(parts[2]);
                            break;
                        case "t_cave_cont":
                            CVars.t_cave_cont=Integer.parseInt(parts[2]);
                            break;
                        default:
                            console.writeError("no var "+parts[1]);
                    }
                }catch(NumberFormatException e){
                    console.writeError("bad number <"+parts[2]+">");
                }
            }else
                switch (parts[1]){
                    case "t_seed":
                        console.write(""+CVars.t_seed);
                        break;
                    case "t_cave_height":
                        console.write(""+CVars.t_cave_height);
                        break;
                    case "t_cave_trim":
                        console.write(""+CVars.t_cave_trim);
                        break;
                    case "t_cave_cont":
                        console.write(""+CVars.t_cave_cont);
                        break;
                    default:
                        console.writeError("no var "+parts[1]);

                }
            return;
        }
        console.writeError("unknown command");
    }

    public static class CVars{
        public static long t_seed=12;
        public static float t_cave_height=0.5f;
        public static float t_cave_trim=0.0f;
        public static int t_cave_cont=0;
    }
}
