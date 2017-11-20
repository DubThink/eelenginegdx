package com.eelengine.engine;

import com.artemis.WorldConfiguration;
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
import org.omg.PortableInterceptor.INACTIVE;

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

    boolean DEBUG_physics_render=true;
    // Temp physics testing


	@Override
	public void create () {
        setupRendering();
        setupPhysics();
        setupECS();
        FontKit.initFonts();
		img = new Texture(Gdx.files.internal("Eel_E_64x.png"),true);
		img.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        bkdimg = new Texture(Gdx.files.internal("semifade_half_black_left.png"));
        bkdimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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
        physicsDomain.step(Gdx.graphics.getDeltaTime(), 20, 20);
        camController.setViewGrabbed(Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
        camController.updatePan(-Gdx.input.getDeltaX(),Gdx.input.getDeltaY());
        camController.update();
        worldBatch.setProjectionMatrix(camController.getCam().combined);
        interfaceCam.update();
        interfaceBatch.setProjectionMatrix(interfaceCam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camController.getCam().combined);
        shapeRenderer.begin();
        DebugView.drawGrid(shapeRenderer,camController.getCam(),true);
//        physicsDebugDrawer(dynamicBody,shapeRenderer);
//        physicsDebugDrawer(staticBody,shapeRenderer);
//        for(Body b:statics)physicsDebugDrawer(b,shapeRenderer);
        shapeRenderer.end();

		worldBatch.begin();
//		/worldBatch.draw(img, 0, 0,100,100);
//        System.out.println(camController.getZoomFactor()+" "+camController.getPos()+" "+Gdx.input.getY());
        worldBatch.end();

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
        if(DEBUG_physics_render) {
            Matrix4 matrix4 = new Matrix4(camController.getCam().combined);
            matrix4.scale(100, 100, 100);
            debugRenderer.render(physicsDomain, matrix4);
        }
	}
	
	@Override
	public void dispose () {
        System.out.println("CLEANING UP FOR EXIT");
		worldBatch.dispose();
		physicsDomain.dispose();
		img.dispose();
		FontKit.dispose();
        System.out.println("EXITING");
    }

	void setupECS(){
        com.artemis.WorldConfiguration entityConfig=new WorldConfiguration();
        //entityConfig.
        entityWorld=new com.artemis.World(entityConfig);
    }
    Body dynamicBody,staticBody;
    ArrayList<Body> statics=new ArrayList<Body>();
    World physicsDomain;
    void setupPhysics(){
        //Box2D.init();

        physicsDomain=new World(new Vector2(0,-9.8f),true);
        dynamicBody= makeThing2(0,8,true);
        //dynamicBody.setBullet(true);
        staticBody=makeThing(0,0,false);
    }

    Body makeThing(float x, float y,boolean dynamic){
        BodyDef myBodyDef=new BodyDef();
        myBodyDef.type = dynamic ? BodyDef.BodyType.DynamicBody:BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(x,y); //set the starting position
        //myBodyDef.angle = 0; //set the starting angle
        Body body = physicsDomain.createBody(myBodyDef);
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
        Body body = physicsDomain.createBody(myBodyDef);
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
                if(keycode==Input.Keys.B)DEBUG_physics_render=!DEBUG_physics_render;
            }else if(keycode==Input.Keys.SPACE) {
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
