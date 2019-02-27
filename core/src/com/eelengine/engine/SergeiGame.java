package com.eelengine.engine;

import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import static java.lang.Math.round;

public class SergeiGame extends EelGame {

    int robot;
    GridWorld gridWorld;
    Table terminalTable;
    Label commandHistoryUI;
    ProgressBar commandBar;
    ScrollPane scrollPane;
    public SergeiGame() {
    }

    public SergeiGame(String level){
        DEV_draw_grid=true;
    }
    @Override
    public void gameCreate() {
        gridWorld=new GridWorld();
        JamFontKit.initFonts();
        assetSystem.finishLoading();
        robot=RobotHelper.makeRobot(entityWorld,physicsWorld);
        playerInput =ECS.mInput.get(robot);

        // SET UP COMMAND LINE
        final TextField textArea = new TextField("Click Me",skin,"default");
//        textArea.setWidth(200);
//        textArea.setHeight(50);
        textArea.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return 32<=c&&c<=126;
            }
        });
        textArea.setBlinkTime(0.2f);
        textArea.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    System.out.println("executing command");
                    ECS.mRobot.get(robot).command=textField.getText();
                    ECS.mRobot.get(robot).commandHistory+="\n"+textField.getText();
                    textField.setText("");
                    scrollPane.setScrollPercentY(1);
                }
            }
        });
        terminalTable=new Table(skin);
//        terminalTable.setFillParent(true);
        terminalTable.setSize(400,800);
        terminalTable.background("window");
        terminalTable.align(Align.bottom);
        stage.addActor(terminalTable);
        commandHistoryUI =new Label("",skin);
        scrollPane=new ScrollPane(commandHistoryUI,skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setScrollingDisabled(true,false);
        commandBar=new ProgressBar(0,1,.01f,false,skin);
        commandBar.setValue(.6f);

        terminalTable.add(scrollPane).prefWidth(11111);
        terminalTable.row();
        terminalTable.add(commandBar).prefWidth(11111);
        terminalTable.row();
        terminalTable.add(textArea).prefWidth(11111);
    }

    @Override
    void loadSystems(WorldConfigurationBuilder worldConfigurationBuilder) {
        super.loadSystems(worldConfigurationBuilder);
        worldConfigurationBuilder.with(robotSystem=new RobotSystem());

    }

    @Override
    public void logicStep() {
        CRobot cRobot=ECS.mRobot.get(robot);
        terminalTable.setVisible(cRobot!=null);
        if(cRobot!=null) {
            commandHistoryUI.setText(cRobot.commandHistory);
            commandBar.setValue(1-cRobot.getCooldownPercent());

        }
        // Update controls

    }

    @Override
    public void render() {
        super.render();
//        Pixmap pixmap=new Pixmap((int)GSCALE*Chunk.SIZE,(int)GSCALE*Chunk.SIZE,RGBA8888);
//        Chunk chunk=gridWorld.getChunk(0,0);
//        pixmap.setColor(0xffffffff);
//        pixmap.fill();
//        for(int x=0;x<16;x++){
//            for(int y=0;y<16;y++) {
//                pixmap.drawPixel(x,y, chunk.tiles[x][y].arid<<24|chunk.tiles[x][y].veg<<16|0x00ff);
//            }
//        }
        worldBatch.begin();
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
        gridWorld.getTile(round(gridPos.x/10),round(gridPos.y/10)).setArid(255);
    }

    @Override
    public void gameKeyDown(int keycode) {
//        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
//        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat"));
        super.gameKeyDown(keycode);
    }
}
