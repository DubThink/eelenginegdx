package com.eelengine.engine;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eelengine.engine.editor.LevelData;
import com.eelengine.engine.editor.LevelIO;
import com.eelengine.engine.editor.MailboxSrc;

public class MailGame extends EelGame {
    public static ComponentMapper<CMailbox> mMailbox;

    int activeZone=1;
    LoadedTextureRegion moneyIcon;
    int car;
    int player;
    boolean playerActive=true;
    String levelToBuild=null;

    LevelData levelData;
    public MailGame() {
    }

    public MailGame(String level){
        levelToBuild=level;
    }
    @Override
    public void gameCreate() {
        mMailbox=entityWorld.getMapper(CMailbox.class);
        System.out.println("IS THERE: "+Gdx.files.internal("sprites/interface/mail_not.png").exists());
        entityWorld.getSystem(MailSystem.class).mailPopup=new Texture(Gdx.files.internal("sprites/interface/mail_not.png"));
        JamFontKit.initFonts();
        // build level
        //setupEditor();
        assetSystem.finishLoading();
        editor.sourceName=levelToBuild+".lvlsrc";
        if(Gdx.files.internal(editor.sourceName).exists()) {
            editor.loadLevel(editor.sourceName);
            //buildLevelStaticSprites(editor.getLevelSource());
        }else {
            System.err.println("ERROR: Unable to load level "+editor.sourceName);
        }

        if(Gdx.files.internal(levelToBuild+".lvldat").exists()) {
            levelData= LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat"));
        }else{
            System.err.println("WARNING: Unable to load level data "+levelToBuild+".lvldat");
            levelData=new LevelData();
        }
        editor.geomEditor.buildStatics(physicsWorld);
        moneyIcon=new LoadedTextureRegion("sprites/interface/dollas.png");
        car=JamEntityBuilder.makeCar(entityWorld,new Texture(Gdx.files.internal("sprites/objects/car.png"),true),physicsWorld);
        entInput=ECS.mInput.get(car);

        player=JamEntityBuilder.makePlayer(entityWorld,physicsWorld);
    }

    @Override
    public void logicStep() {
        // Update controls
        entInput=ECS.mInput.get(playerActive?player:car);
        ECS.mInput.get(car).enabled=!playerActive;

        if(!freeCam){
            Vector2 camPos=ECS.mTransform.get(playerActive?player:car).pos;
            camController.setPos(camPos.x*GSCALE,camPos.y*GSCALE);
            camController.setZoomLevel(playerActive?-3:-1);
        }
    }

    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        float topThird=2*height/3;
        interfaceBatch.begin();
//        renderCentered(interfaceBatch,JamFontKit.SysMonolithic,playerActive+"",width/2,height/2);
        interfaceBatch.draw(moneyIcon,0,0);

        if(playerActive&&triggerSystem.checkFlag("CAR",ECS.mTransform.get(player).pos)!=-1){
            renderCentered(interfaceBatch,JamFontKit.SysLarge,"T to enter vehicle",width/2, topThird);
        }
        if(!playerActive)renderCentered(interfaceBatch,JamFontKit.SysLarge,"T to exit vehicle",width/2, topThird);

        int mb=triggerSystem.checkFlag("MAILBOX",getWorldMouse());
        if(mb>-1)renderCentered(interfaceBatch,JamFontKit.SysLarge,"Mailbox",width/2,40);
        interfaceBatch.end();
        super.renderUI();
    }

    public void renderCentered(SpriteBatch batch, BitmapFont font, String text, float x, float y){
        GlyphLayout layout = new GlyphLayout(font,text);
        font.draw(batch,text,x-layout.width/2,y+layout.height/2);
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }

    @Override
    public void clickEvent(Vector2 wp) {
        super.clickEvent(wp);
        int mb=triggerSystem.checkFlag("MAILBOX",wp);
        if(mb==-1)return;
        ECS.mMailbox.get(mb).mailCt++;
    }

    @Override
    public void gameKeyDown(int keycode) {
        if(keycode==Input.Keys.S&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))LevelIO.saveLevelData(Gdx.files.internal(levelToBuild+".lvldat"),levelData);
        if(keycode==Input.Keys.O&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))levelData=LevelIO.loadLevelData(Gdx.files.internal(levelToBuild+".lvldat"));
        if(keycode==Input.Keys.T){
            if(playerActive){
                if(triggerSystem.checkFlag("CAR",ECS.mTransform.get(player).pos)==-1)return;
                // stash the player far far away
                ECS.mPhysics.get(player).body.setTransform(-1000,0,0);
                playerActive=false;
                ECS.mInput.get(player).clearOn();
            }else{
                // bring the player to the car
                ECS.mPhysics.get(player).body.setTransform(ECS.mTransform.get(car).pos,0);
                playerActive=true;
                ECS.mInput.get(car).clearOn();
            }
        }else if(keycode==Input.Keys.C){
            freeCam =!freeCam;
        }else if(keycode==Input.Keys.M&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                buildMailboxes();
            }else{
                System.out.println(levelData);
                System.out.println(levelData.mailboxSrcs);
                levelData.mailboxSrcs.add(new MailboxSrc(getWorldMouse(),1));
            }
        }else
        super.gameKeyDown(keycode);
    }
    public void buildMailboxes(){
        System.out.printf("Building %d Mailboxes\n",levelData.mailboxSrcs.size());
        mailSystem.clearAll();
        for(MailboxSrc src:levelData.mailboxSrcs){
            if(src.zone<=activeZone){
                JamEntityBuilder.makeMailbox(entityWorld,src.location);
            }
        }
    }
}
