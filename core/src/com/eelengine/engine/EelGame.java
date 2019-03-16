package com.eelengine.engine;

import bpw.Util;
import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eelengine.engine.ai.Navigation;
import com.eelengine.engine.editor.Editor;
import com.eelengine.engine.editor.LevelSource;
import com.eelengine.engine.robot.RobotSystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The core class of the engine
 * LONG BOI
 */
public class EelGame extends ApplicationAdapter implements InputProcessor {
    // CONSTANTS
    public static final int GSCALE=32;
    public static final float GSCALE_F=(float)GSCALE;
    public static final float GSCALE_HALF=GSCALE_F/2;
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int VIRTUAL_WINDOWED_WIDTH = 1600;
    public static final int VIRTUAL_WINDOWED_HEIGHT = 900;

    // RENDERERS
    PolygonSpriteBatch worldBatch; // The primary batch for rendering to worldspace
    SpriteBatch interfaceBatch; // Screenspace
    ShapeRenderer shapeRenderer; // Debug renderer (worldspace)
    Box2DDebugRenderer debugRenderer; // Physics debug renderer object
    Stage stage;

    // RENDER SOURCES
    ArrayList<StaticSprite> staticLayer0 =new ArrayList<>();
    ArrayList<StaticSprite> staticLayer1=new ArrayList<>();
    ArrayList<StaticSprite> staticLayer2=new ArrayList<>();
    // two iterators running towards each other like romantic lovers on a beach
    // VIEW
    OrthographicCamera interfaceCam;
    CamController camController;
    Viewport viewport;

    // ECS
    com.artemis.World entityWorld;
    RenderOneTexSystem spriteRenderSystem;
    TriggerSystem triggerSystem;
    RobotSystem robotSystem;

    // PHYSICS
    World physicsWorld;

    // SYSTEMS
    Sound screenshotSound;
    Navigation navigation;
    AssetSystem assetSystem=new AssetSystem();

    // EDITING
    Editor editor;

    // UI
    Skin skin;

    // TEST ASSETS
    Texture img,img2;
    Texture bkdimg;
    StaticSprite testSprite;

    // TEST MISC
    int entityCount;

    // DEV SWITCHES
    boolean DEV_physics_render =false;
    boolean DEV_draw_grid=false;
    boolean DEV_draw_nav=false;
    float DEV_time_mod=1f;

    // VISUAL ELEMENT SWITCHES
    boolean fullscreen=false;
    boolean escapeMenu=false;
    boolean freeCam =true;

    // ENTITY CONTROL
    CInput playerInput;

    /**
     * Runs on start- the setup function
     */
    @Override
    public void create () {
        img = new Texture(Gdx.files.internal("test_car_lol.png"),true);
        img2 = new Texture(Gdx.files.internal("blic_32x.png"),true);
        img.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        img2.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        bkdimg = new Texture(Gdx.files.internal("semifade_half_black_left.png"));
        bkdimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        screenshotSound=Gdx.audio.newSound(Gdx.files.internal("interface/camera-shutter.wav"));

        setup();

        setupRendering();

        stage = new Stage();



        TextureAtlas atlas = new TextureAtlas(Gdx.files.local("skin/uiskin.atlas"));
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"),atlas);

        BitmapFont labelFont = skin.get("commodore-64", BitmapFont.class);
        labelFont.getData().markupEnabled = true;

//        list.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                List l=(List)actor;
//                if(l.getSelected()=="Edit"){
//                    editorEnabled = true;
//                    if (geomEditor == null) setupEditor();
//                }else editorEnabled=false;
//            }
//        });

        setupEditor();
        setupPhysics();
        navigation=new Navigation(Gdx.files.internal("maps/map2.nav"));
        setupECS();
        FontKit.initFonts();
        debugRenderer = new Box2DDebugRenderer();
        testSprite=new StaticSprite("sig.png");
//        navPath=navigation.findPath(ECS.mTransform.get(ent).pos,new Vector2(
//                camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY())));
        gameCreate(); // Call the overrided init function
    }

    /**
     * Initializer function for games to override
     */
    public void gameCreate(){}

    private void setup() {
        LoadedTextureRegion.assetSystem=assetSystem;
    }

    @Override
    public void resize(int width,int height){
        camController.setOrtho(width,height);
        interfaceCam.setToOrtho(false,width,height);
        viewport.setScreenSize(width,height);
        viewport.update(width, height,true);
        stage.getViewport().update(width, height,false);
    }

    /** to override*/
    public void renderWorld(){
        // Render statics
        worldBatch.begin();
        for (StaticSprite sprite : editor.getLevelSource().staticLayer0) {
            RenderUtil.renderSprite(worldBatch,sprite.region,sprite.pos.x, sprite.pos.y,sprite.rot);
        }
        for (StaticSprite sprite : editor.getLevelSource().staticLayer1) {
            RenderUtil.renderSprite(worldBatch,sprite.region,sprite.pos.x, sprite.pos.y,sprite.rot);
        }
        worldBatch.end();

    }

    /**
     * Don't override this
     * put your code in one of the like 5 other functions
     * unless you really need to
     * but you don't
     */
    @Override
    public void render () {
        // ----- PRE RENDER ----- //
        // Update the asset loader before anything else
        boolean loading=!assetSystem.update();
//        System.out.println(assetSystem.getQueuedAssets()+":"+assetSystem.getLoadedAssets());

        // clear graphics
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera systems
        if(freeCam) {
            camController.setViewGrabbed(Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
            camController.updatePan(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
        }else camController.setViewGrabbed(false);
        camController.update();
        interfaceCam.update();
        interfaceBatch.setProjectionMatrix(interfaceCam.combined);
        Matrix4 matrix4 = new Matrix4(camController.getCam().combined);
        matrix4.scl(EelGame.GSCALE);
        worldBatch.setProjectionMatrix(camController.getCam().combined);
        shapeRenderer.setProjectionMatrix(matrix4);

        // ----- RENDER ----- //

        renderWorld();

        // Draw grid
        if (DEV_draw_grid) {
            shapeRenderer.begin();
            DebugView.drawGrid(shapeRenderer,camController.getCam(),false);
            shapeRenderer.end();
        }

        // Draw debug for nav
        if(DEV_draw_nav) {
            navigation.drawCells(shapeRenderer);
        }

        // Step ECS
        entityWorld.setDelta(Gdx.graphics.getDeltaTime()*DEV_time_mod);
        entityWorld.process();

        // Call game-specific logic (overridden usually)
        logicStep();
//            navPath=navigation.findPath(ECS.mTransform.get(ent).pos,
//                    new Vector2(camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY())));
//            if(navPath!=null)navPath.draw(shapeRenderer);
////            else System.out.println("NO NAV");
//        entNavigator.targetPoint =new Vector2(camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY()));
//        }

//        System.out.println("Are we drawing? "+worldBatch.isDrawing());

        worldBatch.begin();
        // Render upper statics
        for (StaticSprite sprite : editor.getLevelSource().staticLayer2) {
            RenderUtil.renderSprite(worldBatch,sprite.region,sprite.pos.x, sprite.pos.y,sprite.rot);
        }
        worldBatch.end();


        // GeomEditor
//        Vector2 ms=camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
        if(editor.mode!=Editor.OFF) editor.render(worldBatch,shapeRenderer,interfaceBatch);

        // Draw physics debug
        if(DEV_physics_render) {
            debugRenderer.render(physicsWorld, matrix4);
        }
//        if(Gdx.graphics.getFrameId()%60==0){
//            makeBloob((int)((Math.random()-0.5)*40),(int)((Math.random()-0.5)*40));
//        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        renderUI();

        // Display dev interface
        interfaceBatch.begin();
        if(escapeMenu){
            interfaceBatch.draw(bkdimg,0,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
            FontKit.SysHuge.setColor(Color.CHARTREUSE);
            FontKit.SysHuge.draw(interfaceBatch,"ESCAPE MENU",10,Gdx.graphics.getHeight()-10);
            FontKit.SysLarge.setColor(Color.FIREBRICK);
            FontKit.SysLarge.draw(interfaceBatch,"Press enter to exit",10,Gdx.graphics.getHeight()-58);
            FontKit.SysMedium.setColor(Color.WHITE);
            FontKit.SysMedium.draw(interfaceBatch,"FPS:"+Gdx.graphics.getFramesPerSecond(),10,Gdx.graphics.getHeight()-88);
            FontKit.SysMedium.draw(interfaceBatch,"Entity count: "+entityCount,10,Gdx.graphics.getHeight()-108);
            FontKit.SysMedium.draw(interfaceBatch,"Cam:"+camController,10,Gdx.graphics.getHeight()-128);
//            FontKit.SysMedium.draw(interfaceBatch,"FPS:"+Gdx.graphics.getFramesPerSecond(),10,Gdx.graphics.getHeight()-148);
            FontKit.SysMedium.draw(interfaceBatch,
                    "F1 to toggle 1/5 speed\n" +
                            "F3+G to toggle grid\n" +
                            "F3+B to toggle physics debug\n" +
                            "F3+N to toggle nav cell drawing\n" +
                            "F9 geometry editor\n" +
                            "F10 sprite editor\n" +
                            "F11 to toggle fullscreen\n" +
                            "F12 for screenshot (saved to assets/screenshots)"
                    ,10,Gdx.graphics.getHeight()-168);
            FontKit.SysSmall.draw(interfaceBatch,
                    camController.getCam().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).toString(),
                    10,22);

        }else {
            FontKit.SysMedium.setColor(Color.FIREBRICK);
            FontKit.SysMedium.draw(interfaceBatch, "Press ESC for menu", 10, Gdx.graphics.getHeight() - 10);
        }
        FontKit.SysMedium.setColor(Color.TEAL);
        if(loading)FontKit.SysMedium.draw(interfaceBatch, "Loading ["+(int)(assetSystem.getProgress()*100)+"%]", Gdx.graphics.getWidth()-140, 20);
        interfaceBatch.end();
    }
    public void logicStep(){}//To Extend
    public void renderUI(){}//To Extend

    @Override
    public void dispose () {
        System.out.println("CLEANING UP FOR EXIT");
        entityWorld.dispose();
        worldBatch.dispose();
        physicsWorld.dispose();
        img.dispose();
        assetSystem.clear();
        FontKit.dispose();
        System.out.println("EXITING");
    }

    void makeBloob(float x,float y){
        int e=entityWorld.create();
        COneTex cOneTex =ECS.mGraphics.create(e);
        ECS.mTransform.create(e)
                .setPos(x,y)
                .setScale(.9f)
                .setRot((float)Math.random()*Util.TWO_PI_F);
//        cOneTex.texture=img2;

//        CPhysics pc=ECS.mPhysics.create(e);
//        pc.buildBody(physicsWorld);
//        pc.body.setType(BodyDef.BodyType.DynamicBody);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(.3f,.3f);
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.restitution=0.5f;
//        pc.body.createFixture(fixtureDef);
//        shape.dispose();
        //pc.body.setLinearDamping(10);
        //pc.body.setAngularDamping(10);
//        Vector2 v=new Vector2((float)Math.random()-0.5f,(float)Math.random()-0.5f).setLength((float)Math.random()*8);
//        pc.body.setLinearVelocity(v);
//        ECS.mHealth.create(e)
//                .set(5);
//        ECS.mDamager.create(e)
//                .set(5);
//        ECS.mProjectile.create(e)
//                .destroyOnHit=true;
//        ECS.mTeam.create(e)
//                .set(1);
        ECS.mTrigger.create(e).addFlags("RED").setRadius(1);
    }

    void makeMailbox(float x,float y){
        int e=entityWorld.create();
        COneTex cOneTex =ECS.mGraphics.create(e);
        ECS.mTransform.create(e)
                .setPos(x,y)
                .setScale(1f);
//        cOneTex.texture=img;

        CPhysics pc=ECS.mPhysics.create(e);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.3f,.3f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution=0.5f;
        pc.body.createFixture(fixtureDef);
        shape.dispose();
        //pc.body.setLinearDamping(10);
        //pc.body.setAngularDamping(10);
        Vector2 v=new Vector2((float)Math.random()-0.5f,(float)Math.random()-0.5f).setLength((float)Math.random()*8);
        pc.body.setLinearVelocity(v);
        ECS.mHealth.create(e)
                .set(5);
        ECS.mDamager.create(e)
                .set(5);
        ECS.mProjectile.create(e)
                .destroyOnHit=true;
        ECS.mTeam.create(e)
                .set(1);
    }

    void makeBullet(float x, float y, float tx, float ty){
        int e=entityWorld.create();
//        ECS.mTrigger.create(e).addFlags("RED").setRadius(2);
        COneTex cOneTex =ECS.mGraphics.create(e);
        CTransform cTransform=ECS.mTransform.create(e)
                .setScale(1.5f)
                .setPos(x,y);
//        cOneTex.texture=img2;
//        cTransform.rotLockedToPhysics=false;
//        cTransform.pointAtVelocity=true;
        CPhysics pc=ECS.mPhysics.create(e);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        pc.body.setBullet(true);
        Vector2 v=new Vector2(tx-x,ty-y).setLength(20);
        cTransform.rot=v.angle()* Util.DEG_TO_RAD_F-Util.HALF_PI_F;
//        System.out.println("Velocity:"+v);
        pc.body.setLinearVelocity(v);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.25f,.25f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;
        pc.body.createFixture(fixtureDef);
        shape.dispose();
        //pc.body.setLinearDamping(10);
        pc.body.setAngularDamping(1);
        pc.setFilter(PHYS.ONE,~PHYS.PLAYERTEAM);
        ECS.mProjectile.create(e)
                .setLifetime(10);
        ECS.mDamager.create(e)
                .set(10);
    }

    /**
     * Override to register your own ECS Systems
     * @param worldConfigurationBuilder
     */
    void loadSystems(WorldConfigurationBuilder worldConfigurationBuilder){
        worldConfigurationBuilder.with(WorldConfigurationBuilder.Priority.HIGH + 1, new BaseSystem() {
            @Override
            protected void processSystem() {
                physicsWorld.step(Gdx.graphics.getDeltaTime()*DEV_time_mod, 6,2);
            }
        })
                .with(WorldConfigurationBuilder.Priority.HIGH,new PhysicsToTransformUpdateSystem())
                .with(spriteRenderSystem=new RenderOneTexSystem(worldBatch))
                .with(triggerSystem=new TriggerSystem())
                .with(new AnimSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,new UtilSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,new HealthSystem(interfaceBatch,camController))
                .with(WorldConfigurationBuilder.Priority.LOW - 1, new BaseEntitySystem(Aspect.all()) {
                    @Override
                    protected void processSystem() {
                        IntBag actives = subscription.getEntities();
                        entityCount=actives.size();
                    }
                })
                .with(WorldConfigurationBuilder.Priority.LOWEST,new PhysicsSystem())
                .with(new MovementSystem())
                .with(new NavigationSystem(navigation,shapeRenderer));
    }

    /**
     * Sets up the Entity-Component-System structure
     * @pre All subsystems used by ECS should be initialized first
     */
    void setupECS(){
        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder();
        loadSystems(worldConfigurationBuilder);
        com.artemis.WorldConfiguration entityConfig=worldConfigurationBuilder.build();
        //entityConfig.
        entityWorld=new com.artemis.World(entityConfig);
        ECS.initialize(entityWorld);
        EntitySubscription subscription = entityWorld.getAspectSubscriptionManager().get(Aspect.all(CPhysics.class));
        triggerSystem.setEnabled(false);
        subscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
            ComponentMapper<CPhysics> mPhysics=entityWorld.getMapper(CPhysics.class);
            ComponentMapper<CTransform> mPosition=entityWorld.getMapper(CTransform.class);

            @Override
            //IntBag is all entities with a new physics component since last ECS update
            public void inserted(IntBag entities) {
                for(int i=0;i<entities.size();i++){
                    int e=entities.get(i);
                    mPhysics.get(e).buildBody(physicsWorld);
                    mPhysics.get(e).body.setUserData(e);
//                    System.out.print("@subs "+mPhysics.get(e).body.getLinearVelocity());
                    if(mPosition.has(e)){
                        CTransform transform=mPosition.get(e);
                        mPhysics.get(e).body
                                .setTransform(transform.pos,transform.rotLockedToPhysics?transform.rot:0);
                    }

                }
            }

            @Override
            public void removed(IntBag entities) {
                for(int i=0;i<entities.size();i++){
                    physicsWorld.destroyBody(mPhysics.get(entities.get(i)).body);
                }
            }
        });
        // PLAYER/TEST CHARACTER
//        ent=entityWorld.create();
//
//        COneTex cOneTex =ECS.mGraphics.create(ent);
//        CTransform cTransform = ECS.mTransform.create(ent);
//        playerInput = ECS.mInput.create(ent);
//        ECS.mMovement.create(ent).setMaxSpeed(4).setVehicular(true);
//
//        cOneTex.texture=img;
//        cOneTex.setOffset(0.4f,0.5f);
//        cTransform.pos.set(2,10);
//        cTransform.setScale(1.25f);
//        CPhysics pc=ECS.mPhysics.create(ent);
//        pc.buildBody(physicsWorld);
//        pc.body.setType(BodyDef.BodyType.DynamicBody);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(.5f,.5f);
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        pc.body.createFixture(fixtureDef);
//        pc.body.setAngularDamping(10);
//        pc.setFilter(PHYS.PLAYERTEAM,PHYS.ALL);
//        // COLLISION TEST GROUND
//        //pc.body.
//        //
//        shape.dispose();
//        cTransform.rotLockedToPhysics=false;
//        ECS.mHealth.create(ent)
//                .set(40);
//        ECS.mDamager.create(ent)
//                .set(100);
//        entNavigator=ECS.mNavigator.create(ent)
//                .setMode(CNavigator.POINT);

        //for(int i=0;i<10;i+=2)makeBloob(i);

        // This is part of ecs really. mostly needs to be done after ecs is up
        physicsWorld.setContactListener(new CollisionHandler(entityWorld) );
    }

    public void buildLevelStaticSprites(LevelSource source){
        staticLayer0.clear();
        staticLayer0.addAll(source.staticLayer0);
        staticLayer1.clear();
        staticLayer1.addAll(source.staticLayer1);
        staticLayer2.clear();
        staticLayer2.addAll(source.staticLayer2);
    }

    void setupPhysics(){
        //Box2D.init();

        physicsWorld =new World(new Vector2(0,0),true);
        //dynamicBody= makeThing2(0,8,true);
        //dynamicBody.setBullet(true);
        //staticBody=makeThing(0,0,false);
    }

    Body makeThing(float x, float y,boolean dynamic){
        BodyDef myBodyDef=new BodyDef();
        myBodyDef.type = dynamic ? BodyDef.BodyType.DynamicBody:BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(x,y); //set the starting position
        //myBodyDef.angle = 0; //set the starting angle
        Body body = physicsWorld.createBody(myBodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2,2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }
    Body makeThing2(float x, float y,boolean dynamic){
        BodyDef myBodyDef=new BodyDef();
        myBodyDef.type = dynamic ? BodyDef.BodyType.DynamicBody:BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(x,y); //set the starting position
        //myBodyDef.angle = 0; //set the starting angle
        Body body = physicsWorld.createBody(myBodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        shape.dispose();
        return body;
    }

    void setupRendering(){
        // Renderers
        worldBatch = new PolygonSpriteBatch();
        interfaceBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        //Cameras
        viewport = new ScreenViewport();
        interfaceCam = new OrthographicCamera(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        camController=new CamController(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        Gdx.graphics.setResizable(false);

        // Fullscreen
        if (!fullscreen) {
            Gdx.graphics.setWindowedMode(VIRTUAL_WINDOWED_WIDTH, VIRTUAL_WINDOWED_HEIGHT);
        } else {
            System.out.println("FULLSCREEN ON");
            Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
            Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
            Gdx.graphics.setFullscreenMode(displayMode);
        }

        Gdx.input.setInputProcessor(this);
    }

    void setupEditor(){
        editor=new Editor(camController);
    }

    /**
     * Convenience function to return mouse pos in world coords
     * @return mouse position in world coordinates
     */
    Vector2 getWorldMouse(){
        return camController.screenToWorld(Gdx.input.getX(),Gdx.input.getY());
    }

    public void gameKeyDown(int keycode){
        // WASD
        if(playerInput !=null) {
            if (keycode == Input.Keys.W) {
                playerInput.down(CInput.UP);
            } else if (keycode == Input.Keys.A) {
                playerInput.down(CInput.LEFT);
            } else if (keycode == Input.Keys.S) {
                playerInput.down(CInput.DOWN);
            } else if (keycode == Input.Keys.D) {
                playerInput.down(CInput.RIGHT);
            }
        }
    }

    // ----- INPUT PROCESSOR ----- //

    @Override
    public boolean keyTyped(char character) {
        return stage.keyTyped(character);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return stage.mouseMoved(screenX,screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(stage.keyDown(keycode)){
            if (keycode == Input.Keys.ESCAPE)
                stage.unfocusAll();
            return true;
        }
        if(keycode==Input.Keys.SHIFT_LEFT){
            editor.shiftDown();
        }

        if (keycode == Input.Keys.ESCAPE) {
            escapeMenu = !escapeMenu;
        }else if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
            if (keycode == Input.Keys.B) DEV_physics_render = !DEV_physics_render;
            if (keycode == Input.Keys.G) DEV_draw_grid = !DEV_draw_grid;
            if (keycode == Input.Keys.N) DEV_draw_nav = !DEV_draw_nav;
//                if(keycode==Input.Keys.F) EelGame.GSCALE=150-EelGame.GSCALE;

        }
        else if (keycode == Input.Keys.F6) {
        }
        // Zoom keys
        else if (keycode == Input.Keys.MINUS) {
            camController.changeZoomLevel(1);
        } else if (keycode == Input.Keys.EQUALS) {
            camController.changeZoomLevel(-1);
        }else if (keycode == Input.Keys.F1) {
            DEV_time_mod = 1.2f - DEV_time_mod;
        } else if (keycode == Input.Keys.SPACE) {
            //dynamicBody.setLinearVelocity(0, 20);
        } else if (keycode == Input.Keys.ENTER) {
            if (escapeMenu) Gdx.app.exit();
            else camController.setPos(0, 0);
        } else if (keycode == Input.Keys.F9) {
            if(editor.mode==Editor.GEOM)editor.mode=Editor.OFF;
            else editor.mode=Editor.GEOM;
        } else if (keycode == Input.Keys.F10) {
            if(editor.mode==Editor.SPRITE)editor.mode=Editor.OFF;
            else editor.mode=Editor.SPRITE;
        } else if (keycode == Input.Keys.F11) {
            if (fullscreen) {
                System.out.println("FULLSCREEN OFF");
                Gdx.graphics.setWindowedMode(VIRTUAL_WINDOWED_WIDTH, VIRTUAL_WINDOWED_HEIGHT);
                fullscreen = false;
            } else {
                System.out.println("FULLSCREEN ON");
                Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
                Gdx.graphics.setFullscreenMode(displayMode);
                fullscreen = true;
            }
        } else if (keycode == Input.Keys.F12) {
            byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
            Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
            BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss_SSS");
            System.out.println(Gdx.files.getLocalStoragePath());
            PixmapIO.writePNG(Gdx.files.local("screenshots/screenshot " + sdf.format(new Date()) + ".png"), pixmap);
            pixmap.dispose();
            screenshotSound.play();
        }else if(editor.mode!=Editor.OFF){
            editor.keyDown(keycode);
        }else gameKeyDown(keycode);
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        if(keycode==Input.Keys.SHIFT_LEFT){
            editor.shiftUp();
        }

        if (keycode == Input.Keys.W) {
            playerInput.up(CInput.UP);
        } else if (keycode == Input.Keys.A) {
            playerInput.up(CInput.LEFT);
        } else if (keycode == Input.Keys.S) {
            playerInput.up(CInput.DOWN);
        } else if (keycode == Input.Keys.D) {
            playerInput.up(CInput.RIGHT);
        }
        return stage.keyUp(keycode); // aaa
    }

    @Override
    public boolean scrolled(int amount) {
        if(stage.scrolled(amount))return true;
        if(freeCam) {
            camController.changeZoomLevel(amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(stage.touchDown(screenX, screenY, pointer, button)){
            System.out.println("input handled by ui");
            return false;
        }
        if (editor.mode!=Editor.OFF) {
            editor.mouseDown(screenX, screenY, button);
            return stage.touchDown(screenX, screenY, pointer, button);
        }
        mouseDown(getWorldMouse(),button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(stage.touchUp(screenX, screenY, pointer, button)){
            System.out.println("input handled by ui");
            return false;
        }
        if (editor.mode!=Editor.OFF) {
            editor.mouseUp(screenX, screenY, button);
        }
        return false;
    }

    public void mouseDown(Vector2 wp,int button){} // TO OVERRIDE
}  