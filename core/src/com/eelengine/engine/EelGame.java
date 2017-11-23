package com.eelengine.engine;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * The core class of the engine
 */
public class EelGame extends ApplicationAdapter {
    // CONSTANTS
    public static final float GSCALE=100;
	SpriteBatch worldBatch;
	SpriteBatch interfaceBatch;
	ShapeRenderer shapeRenderer;
	com.artemis.World entityWorld;
	Texture img;
	Texture bkdimg;
    OrthographicCamera interfaceCam;
    CamController camController;
    Viewport viewport;
    Box2DDebugRenderer debugRenderer;

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int VIRTUAL_WINDOWED_WIDTH = 1600;
    public static final int VIRTUAL_WINDOWED_HEIGHT = 900;

    boolean DEV_physics_render =true;
    boolean DEV_draw_grid=true;
    float DEV_time_mod=0.2f;
    // Temp physics testing


	@Override
	public void create () {
		img = new Texture(Gdx.files.internal("Eel_E_64x.png"),true);
		img.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        bkdimg = new Texture(Gdx.files.internal("semifade_half_black_left.png"));
        bkdimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setupRendering();
        setupPhysics();
        setupECS();
        FontKit.initFonts();
        debugRenderer = new Box2DDebugRenderer();
	}

    @Override
    public void resize(int width,int height){
        camController.setOrtho(width,height);
        interfaceCam.setToOrtho(false,width,height);
        viewport.setScreenSize(width,height);
        viewport.update(width, height,true);
    }

    boolean fullscreen=true;
    boolean escapeMenu=false;
    private void handleInput() {
//        float yv=Gdx.input.isKeyPressed(Input.Keys.W)?1:Gdx.input.isKeyPressed(Input.Keys.S)?-1:0;
//        float xv=Gdx.input.isKeyPressed(Input.Keys.D)?1:Gdx.input.isKeyPressed(Input.Keys.A)?-1:0;
//        yv=Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.S)?yv:dynamicBody.getLinearVelocity().y;
//        xv=Gdx.input.isKeyPressed(Input.Keys.A)||Gdx.input.isKeyPressed(Input.Keys.D)?xv:dynamicBody.getLinearVelocity().x;
//        dynamicBody.setLinearVelocity(xv,yv);
    }

    void physicsDebugDrawer(Body body, ShapeRenderer shapeRenderer){
        if(!shapeRenderer.isDrawing()){
            System.err.println("Trying to draw but the shapeRenderer is disabled");
            return;
        }
        Vector2 v1=new Vector2();
        Vector2 v2=new Vector2();
        shapeRenderer.setColor(1,1,0,1);
        shapeRenderer.identity();
        shapeRenderer.translate(body.getPosition().x*EelGame.GSCALE,body.getPosition().y*EelGame.GSCALE,0);
        for(Fixture f:body.getFixtureList()){
//            System.out.println("debugrender "+f.getType()+" "+f.getShape().getType());
            switch(f.getType()){
                case Circle:
                    CircleShape circleShape=(CircleShape)f.getShape();
                    shapeRenderer.circle(circleShape.getPosition().x*EelGame.GSCALE,
                            circleShape.getPosition().y*EelGame.GSCALE,
                            circleShape.getRadius()*EelGame.GSCALE);
                    break;
                case Polygon:
                    PolygonShape polygonShape=(PolygonShape)f.getShape();
                    if(polygonShape.getVertexCount()<2)break;
//                    System.out.println("Drawing poly");
                    for(int i=0;i<polygonShape.getVertexCount()-1;i++){
                        polygonShape.getVertex(i,v1);
                        polygonShape.getVertex(i+1,v2);
                        shapeRenderer.line(v1.x*EelGame.GSCALE,
                                v1.y*EelGame.GSCALE,
                                v2.x*EelGame.GSCALE,
                                v2.y*EelGame.GSCALE);
                    }
                    polygonShape.getVertex(polygonShape.getVertexCount()-1,v1);
                    polygonShape.getVertex(0,v2);
                    shapeRenderer.line(v1.x*EelGame.GSCALE,
                            v1.y*EelGame.GSCALE,
                            v2.x*EelGame.GSCALE,
                            v2.y*EelGame.GSCALE);
                    break;
                //case Edge:

                case Chain:
                    ChainShape chainShape=(ChainShape)f.getShape();
                    if(chainShape.getVertexCount()<2)break;
//                    System.out.println("Drawing poly");
                    for(int i=0;i<chainShape.getVertexCount()-1;i++){
                        chainShape.getVertex(i,v1);
                        chainShape.getVertex(i+1,v2);
                        shapeRenderer.line(v1.x*EelGame.GSCALE,
                                v1.y*EelGame.GSCALE,
                                v2.x*EelGame.GSCALE,
                                v2.y*EelGame.GSCALE);
                    }
                    chainShape.getVertex(chainShape.getVertexCount()-1,v1);
                    chainShape.getVertex(0,v2);
                    shapeRenderer.line(v1.x*EelGame.GSCALE,
                            v1.y*EelGame.GSCALE,
                            v2.x*EelGame.GSCALE,
                            v2.y*EelGame.GSCALE);
            }
        }
        shapeRenderer.identity();
    }
	@Override
	public void render () {
        handleInput();

        // clear graphics
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Step physics
        physicsWorld.step(Gdx.graphics.getDeltaTime()*DEV_time_mod, 6,2);

        // Update camera systems
        camController.setViewGrabbed(Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
        camController.updatePan(-Gdx.input.getDeltaX(),Gdx.input.getDeltaY());
        camController.update();
        worldBatch.setProjectionMatrix(camController.getCam().combined);
        interfaceCam.update();
        interfaceBatch.setProjectionMatrix(interfaceCam.combined);
        shapeRenderer.setProjectionMatrix(camController.getCam().combined);

        // Step ECS
        entityWorld.setDelta(Gdx.graphics.getDeltaTime()*DEV_time_mod);
        entityWorld.process();

        // Draw grid
        if (DEV_draw_grid) {
            shapeRenderer.begin();
            DebugView.drawGrid(shapeRenderer,camController.getCam(),true);
            shapeRenderer.end();
        }

        // Display dev interface
        interfaceBatch.begin();
        if(escapeMenu){
            interfaceBatch.draw(bkdimg,0,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
            FontKit.SysHuge.setColor(Color.CHARTREUSE);
            FontKit.SysHuge.draw(interfaceBatch,"ESCAPE MENU",10,Gdx.graphics.getHeight()-10);
            FontKit.SysLarge.draw(interfaceBatch,"Press enter to exit",10,Gdx.graphics.getHeight()-58);
            FontKit.SysMedium.draw(interfaceBatch,""+dynamicBody.getPosition(),10,Gdx.graphics.getHeight()-78);
            FontKit.SysMedium.draw(interfaceBatch,""+staticBody.getPosition(),10,Gdx.graphics.getHeight()-98);
            FontKit.SysSmall.draw(interfaceBatch,
                    camController.getCam().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).toString(),
                    10,22);

        }
        interfaceBatch.end();

        // Draw physics debug
        if(DEV_physics_render) {
            Matrix4 matrix4 = new Matrix4(camController.getCam().combined);
            matrix4.scale(100, 100, 100);
            debugRenderer.render(physicsWorld, matrix4);
        }
	}

	int ent;
    CInput entInput;
	@Override
	public void dispose () {
        System.out.println("CLEANING UP FOR EXIT");
        entityWorld.dispose();
		worldBatch.dispose();
		physicsWorld.dispose();
		img.dispose();
		FontKit.dispose();
        System.out.println("EXITING");
    }

    SpriteRenderSystem spriteRenderSystem;
    /**
     * Sets up the Entity-Component-System structure
     * @pre All subsystems used by ECS should be initialized first
     */
    void setupECS(){
        com.artemis.WorldConfiguration entityConfig=new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGH,new PhysicsToTransformUpdateSystem())
                .with(spriteRenderSystem=new SpriteRenderSystem(worldBatch))
                .with(WorldConfigurationBuilder.Priority.LOWEST,new PhysicsSystem())
                .with(new MovementInputSystem())
                .build();
        //entityConfig.
        entityWorld=new com.artemis.World(entityConfig);
        ECS.initialize(entityWorld);
        EntitySubscription subscription = entityWorld.getAspectSubscriptionManager().get(Aspect.all(CPhysics.class));

        subscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
            ComponentMapper<CPhysics> mPhysics=entityWorld.getMapper(CPhysics.class);
            ComponentMapper<CTransform> mPosition=entityWorld.getMapper(CTransform.class);

            @Override
            //IntBag is all entities with a new physics component since last ECS update
            public void inserted(IntBag entities) {
                for(int i=0;i<entities.size();i++){
                    System.out.println("Subscriber "+entities.get(i)+" "+mPhysics.get(entities.get(i)));
                    mPhysics.get(entities.get(i)).buildBody(physicsWorld);
                    if(mPosition.has(entities.get(i))){
                        CTransform transform=mPosition.get(entities.get(i));
                        System.out.println(transform);
                        System.out.println(mPhysics.get(entities.get(i)).body.getPosition());
                        mPhysics.get(entities.get(i)).body
                                .setTransform(transform.pos,transform.rotLockedToPhysics?transform.rot:0);
                        System.out.println(mPhysics.get(entities.get(i)).body.getPosition());
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

        ent=entityWorld.create();

        CGraphics cGraphics =ECS.mGraphics.create(ent);
        CTransform cTransform = ECS.mTransform.create(ent);
        entInput = ECS.mInput.create(ent);
        cGraphics.texture=img;
        cTransform.pos.set(2,10);
        cTransform.setScale(2.5f);
        CPhysics pc=ECS.mPhysics.create(ent);
        pc.buildBody(physicsWorld);
        pc.body.setType(BodyDef.BodyType.DynamicBody);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        pc.body.createFixture(fixtureDef);
        shape.dispose();
        cTransform.rotLockedToPhysics=false;
        int e2=entityWorld.create();
        ECS.mPhysics.create(e2);
    }
    Body dynamicBody,staticBody;
    ArrayList<Body> statics=new ArrayList<Body>();
    World physicsWorld;
    void setupPhysics(){
        //Box2D.init();

        physicsWorld =new World(new Vector2(0,0),true);
        dynamicBody= makeThing2(0,8,true);
        //dynamicBody.setBullet(true);
        staticBody=makeThing(0,0,false);
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
        worldBatch = new SpriteBatch();
        interfaceBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        //Cameras
        viewport = new ScreenViewport();
        interfaceCam = new OrthographicCamera(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        camController=new CamController(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        // Fullscreen
        Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        Gdx.graphics.setFullscreenMode(displayMode);
        Gdx.graphics.setResizable(false);
        Gdx.input.setInputProcessor(new InputCore(camController));
    }
    class InputCore extends InputAdapter {

        CamController camController;

        InputCore(CamController camController){
            this.camController = camController;
        }

// I deleted useless methods for the sake of keeping this short.


        @Override
        public boolean keyDown(int keycode) {
            if (keycode==Input.Keys.ESCAPE) {
                escapeMenu = !escapeMenu;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.F3)){
                if(keycode==Input.Keys.B) DEV_physics_render =!DEV_physics_render;
                if(keycode==Input.Keys.G) DEV_draw_grid =!DEV_draw_grid;

            }
            // WASD
            else if(keycode==Input.Keys.W){
                entInput.down(CInput.UP);
            }else if(keycode==Input.Keys.A){
                entInput.down(CInput.LEFT);
            }else if(keycode==Input.Keys.S){
                entInput.down(CInput.DOWN);
            }else if(keycode==Input.Keys.D){
                entInput.down(CInput.RIGHT);
            }
            else if(keycode==Input.Keys.Z){
                /////////////
                // TEST SPACE
                Body body=statics.get(statics.size()-1);
                if(body.getType()==BodyDef.BodyType.DynamicBody)
                    body.setType(BodyDef.BodyType.StaticBody);
                else body.setType(BodyDef.BodyType.DynamicBody);

                /////////////
            }else if(keycode==Input.Keys.X) {
                /////////////
                // TEST SPACE
//                System.out.println("S PRES");
//                spriteRenderSystem.setEnabled(!spriteRenderSystem.isEnabled());
                //ComponentMapper<CPhysics> mPhysics=entityWorld.getMapper(CPhysics.class);
                //Body body=
                        //mPhysics.get(ent).body.setType(BodyDef.BodyType.DynamicBody);
//                if(body.getType()==BodyDef.BodyType.DynamicBody)
//                    body.setType(BodyDef.BodyType.StaticBody);
//                else body.setType(BodyDef.BodyType.DynamicBody);
//                Array<Body> bodies = new Array<Body>();
//                physicsWorld.getBodies(bodies);
//                for (Body body : bodies) {
//                }
                /////////////
            }
            else if(keycode==Input.Keys.SPACE) {
                dynamicBody.setLinearVelocity(0, 20);
                return true;
            }else if(keycode== Input.Keys.ENTER){
                if(escapeMenu)Gdx.app.exit();
                else camController.setPos(0,0);
            }else if(keycode==Input.Keys.F11){
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
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            if(keycode==Input.Keys.W){
                entInput.up(CInput.UP);
            }else if(keycode==Input.Keys.A){
                entInput.up(CInput.LEFT);
            }else if(keycode==Input.Keys.S){
                entInput.up(CInput.DOWN);
            }else if(keycode==Input.Keys.D){
                entInput.up(CInput.RIGHT);
            }else return false;
            return true; // aaa
        }

        @Override
        public boolean scrolled(int amount) {

            if(amount == 1){
                camController.changeZoomLevel(1);
            }
            else if(amount == -1){
                camController.changeZoomLevel(-1);
            }

            return false;

        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // origin top left
            Vector3 wx=camController.getCam().unproject(new Vector3(screenX,screenY,0));
            wx.scl(1/EelGame.GSCALE);
            //return super.touchDown(screenX, screenY, pointer, button);
            System.out.println(String.format("Button: (%d,%d) world (%.3f,%.3f) ptr: %d button: %d",
                    screenX,screenY,wx.x,wx.y,pointer, button));
            if(button==2){
                statics.add(makeThing(wx.x,wx.y,false));
            }
            return false;
        }
    }
}
