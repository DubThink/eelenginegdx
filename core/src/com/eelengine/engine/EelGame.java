package com.eelengine.engine;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The core class of the engine
 */
public class EelGame extends ApplicationAdapter {
	SpriteBatch worldBatch;
	SpriteBatch interfaceBatch;
	Texture img;
    OrthographicCamera interfaceCam;
    CamController camController;
    Viewport viewport;

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int VIRTUAL_WINDOWED_WIDTH = 1600/2;
    public static final int VIRTUAL_WINDOWED_HEIGHT = 900/2;

	@Override
	public void create () {
		worldBatch = new SpriteBatch();
		interfaceBatch = new SpriteBatch();
		img = new Texture(Gdx.files.internal("Eel_E_64x.png"),true);
		img.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        FontKit.initFonts();
        viewport = new ScreenViewport();
        interfaceCam = new OrthographicCamera(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        //interfaceCam.position.x=-1000;
        camController=new CamController(VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
        // Fullscreen
        Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
        Gdx.graphics.setFullscreenMode(displayMode);
        Gdx.graphics.setResizable(false);
        Gdx.input.setInputProcessor(new InputCore(camController));
	}

    @Override
    public void resize(int width,int height){
        camController.setOrtho(width,height);
        interfaceCam.setToOrtho(false,width,height);
        viewport.setScreenSize(width,height);
        viewport.update(width, height,true);
    }

    boolean wasPressed=false;
    boolean wasPressed2=false;
    boolean akey=false;
    boolean fullscreen=true;
    boolean escapeMenu=false;
    private void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            if(escapeMenu)Gdx.app.exit();
            else camController.setPos(0,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) != wasPressed2) {
            if(!wasPressed2)escapeMenu = !escapeMenu;
            wasPressed2 = !wasPressed2;
            System.out.println("ESC");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) != wasPressed) {
            akey = !wasPressed;
            wasPressed = !wasPressed;
            if (akey) {
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
        }
    }
    float viewX=0,viewY=0;

	@Override
	public void render () {
        handleInput();
        camController.setViewGrabbed(Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
        camController.updatePan(-Gdx.input.getDeltaX(),Gdx.input.getDeltaY());
        camController.update();
        worldBatch.setProjectionMatrix(camController.getCam().combined);
        interfaceCam.update();
        interfaceBatch.setProjectionMatrix(interfaceCam.combined);
		Gdx.gl.glClearColor(.25f, .25f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldBatch.begin();
		worldBatch.draw(img, -20, -20,40,40);
        System.out.println(camController.getZoomFactor()+" "+camController.getPos()+" "+Gdx.input.getY());
        worldBatch.end();
        interfaceBatch.begin();
        if(escapeMenu){
            FontKit.UtilHuge.setColor(Color.BLUE);
            FontKit.UtilHuge.draw(interfaceBatch,"ESCAPE MENU",10,Gdx.graphics.getHeight()-10);
            FontKit.UtilLarge.draw(interfaceBatch,"Press enter to exit",10,Gdx.graphics.getHeight()-58);
//            FontKit.UtilSmall.draw(batch,
//                    cam.project(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).toString(),
//                    10,22);

        }
        interfaceBatch.end();
	}
	
	@Override
	public void dispose () {
        System.out.println("EXITING");
		worldBatch.dispose();
		img.dispose();
	}
}
 class InputCore extends InputAdapter {

    CamController camController;

    public InputCore(CamController camController){
        this.camController = camController;
    }

// I deleted useless methods for the sake of keeping this short.


    @Override
    public boolean scrolled(int amount) {

        if(amount == 1){
            camController.changeZoomLevel(-1);
        }
        else if(amount == -1){
            camController.changeZoomLevel(1);
        }

        return false;

    }
}