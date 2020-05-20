package com.eelengine.engine.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eelengine.engine.EelGame;
import com.eelengine.engine.MailGame;
import com.eelengine.engine.SergeiGame;
import com.eelengine.engine.StunnedGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.
		//config.foregroundFPS = 288; // 2x 144hz // set to 0 for no frame limit
        //config.backgroundFPS = 15; // Use 0 to never sleep, -1 to not render.
//        config.vSyncEnabled=true;
//        config.resizable=false;
//        config.title="EelEngine Alpha";
//        //config.samples=2;
        config.setWindowIcon(Files.FileType.Internal,"Eel_E_128x.png");
//        config.addIcon("Eel_E_32x.png", Files.FileType.Internal);
//        config.addIcon("Eel_invE_16x.png", Files.FileType.Internal);
        //config.stencil=8; // LOOK we can have stencil bits
		//new Lwjgl3Application(new EelGame(), config);
//		System.out.println("ARG: "+arg.length);
		if(arg.length==1)
			new LwjglApplication(new SergeiGame(arg[0]), config);
		else
			new LwjglApplication(new SergeiGame(), config);
		//app.getGraphics().
	}
}
