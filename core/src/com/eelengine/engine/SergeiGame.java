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
import com.eelengine.engine.robot.RobotSystem;

import static java.lang.Math.round;

public class SergeiGame extends EelGame {

    int robot;
    GridWorld gridWorld;

    // UI
    Table terminalTable;
    Label cmdHistoryUI;
    ProgressBar cmdProgressBar;
    ScrollPane scrollPane;
    TextField cmdTextField;
    StaticNoise2D terrainRenderNoise=new StaticNoise2D(0);

    LoadedTextureRegion terrain,ore;
    public SergeiGame() {
    }

    public SergeiGame(String level){
        DEV_draw_grid=true;
    }
    @Override
    public void gameCreate() {
        initializeTerrain();
        JamFontKit.initFonts();
        assetSystem.finishLoading();
        robot=RobotHelper.makeRobot(entityWorld,physicsWorld);
        playerInput =ECS.mInput.get(robot);

        // SET UP COMMAND LINE
        cmdTextField = new TextField("Click Me",skin,"default");
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

        terrain=new LoadedTextureRegion("rok2.png");
        ore=new LoadedTextureRegion("ore.png");

    }

    void initializeTerrain(){
        gridWorld=new GridWorld(CVars.t_seed);
        // TODO make less hacky
        entityWorld.getSystem(RobotSystem.class).gridWorld=gridWorld;
    }

    @Override
    void loadSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.loadSystems(worldConfigurationBuilder);
        worldConfigurationBuilder.with(robotSystem=new RobotSystem(gridWorld,this));

    }

    @Override
    public void logicStep() {
        CRobot cRobot=ECS.mRobot.get(robot);
        terminalTable.setVisible(cRobot!=null);
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
//        Pixmap pixmap=new Pixmap(GSCALE*Chunk.SIZE,GSCALE*Chunk.SIZE,RGBA8888);
//        pixmap.fill();
//        for(int x=0;x<16;x++){
//            for(int y=0;y<16;y++) {
//                pixmap.drawPixel(x,y, chunk.tiles[x][y].arid<<24|chunk.tiles[x][y].veg<<16|0x00ff);
//            }
//        }

        worldBatch.begin();
        renderChunk(0,0);
        renderChunk(1,0);
        renderChunk(0,1);
        renderChunk(1,1);

//        Texture texture=new Texture(pixmap);
//        System.out.println();
//        System.out.println(ECS.mTransform.get(robot).pos);
//        System.out.println(ECS.mTransform.get(robot).pos);
//        worldBatch.draw(texture,0,0);
        //252, 243, 123
        //66, 51, 27
        //186, 192,96
//        Tile tile;
//        int c=(int)(252-186*(float)tile.arid)<<24|(int)(243-192*(float)tile.arid)<<16|(int)(123-96*(float)tile.arid)<<8|0xff;
//        worldBatch.draw
        worldBatch.end();
    }

    protected void renderChunk(int u, int v){
        Chunk chunk=gridWorld.getChunk(u,v);
        for(int x=0;x<16;x++){
            for(int y=0;y<16;y++) {
                switch (chunk.tiles[x][y].baseResource) {
                    case SAND:
                        worldBatch.setColor(Color.GOLDENROD);
                        break;
                    case ROCK:
                        worldBatch.setColor(Color.GRAY);
                        break;
                    case DIRT:
                        worldBatch.setColor(Color.BROWN);
                        break;
                    default:
                        worldBatch.setColor(Color.RED);

                }
                int ax=x + u*Chunk.SIZE;
                int ay=y + v*Chunk.SIZE;
                worldBatch.draw(terrain.getTexture(),
                        ax*GSCALE,ay*GSCALE,
                        GSCALE*(terrainRenderNoise.test(ax,ay,1)?0:1),GSCALE*(terrainRenderNoise.test(ax,ay,2)?0:1),
                        GSCALE, GSCALE);
                int p=chunk.tiles[x][y].getPrimaryCount();
                if(p>0) {
                    if (chunk.tiles[x][y].getPrimaryResource() == Resource.COPPER) {
                        worldBatch.setColor(Color.FIREBRICK);
                    } else {
                        worldBatch.setColor(Color.DARK_GRAY);
                    }
                    worldBatch.draw(ore.getTexture(), (u*Chunk.SIZE+x)*GSCALE,(v*Chunk.SIZE+y)*GSCALE, GSCALE*(p<8?0:1),GSCALE*(p%8<4?0:1), GSCALE, GSCALE);
                }
//                pixmap.drawPixel(x,y, chunk.tiles[x][y].arid<<24|chunk.tiles[x][y].veg<<16|0x00ff);
            }
        }
    }
    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        float topThird=2*height/3;
        interfaceBatch.begin();


        gridWorld.getTile(getWorldMouse());

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
                }else if(keycode==Input.Keys.DOWN){
                    cmdTextField.setText(ECS.mRobot.get(robot).nextCommand());
                }
            }
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
        Vector3 gridPos=camController.getCam().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
        gridWorld.getTile(round(gridPos.x/10),round(gridPos.y/10));
    }

    @Override
    public void gameKeyDown(int keycode) {
//        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
//        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat"));
        super.gameKeyDown(keycode);
    }

    public void parseSudoCmd(CRobot console, String[] parts){
        if(parts.length<2||!parts[0].equals("sudo"))return;
        if(parts[1].equals("kill")){
            Gdx.app.exit();
            return;
        }
        if(parts[1].equals("regenerate")){
            initializeTerrain();
            console.write("Regenerating world...");
            return;
        }
        if(parts[1].equals("t_seed")){
            if(parts.length>=3){
                // set seed
                try {
                    CVars.t_seed=Long.parseLong(parts[2]);
                }catch(NumberFormatException e){
                    console.writeError("bad number <"+parts[2]+">");
                }
            }else
                console.write(""+CVars.t_seed);
            return;
        }
        console.writeError("unknown command");
    }

    public static class CVars{
        public static long t_seed=0;
    }
}
