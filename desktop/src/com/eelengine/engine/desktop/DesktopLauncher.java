package com.eelengine.engine.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.eelengine.engine.EelGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.
		//config.foregroundFPS = 288; // 2x 144hz // set to 0 for no frame limit
        //config.backgroundFPS = 15; // Use 0 to never sleep, -1 to not render.
//        config.vSyncEnabled=true;
//        config.resizable=false;
//        config.title="EelEngine Alpha";
//        //config.samples=2;
//        config.addIcon("Eel_E_128x.png", Files.FileType.Internal);
//        config.addIcon("Eel_E_32x.png", Files.FileType.Internal);
//        config.addIcon("Eel_invE_16x.png", Files.FileType.Internal);
        //config.stencil=8; // LOOK we can have stencil bits
        new Lwjgl3Application(new EelGame(), config);
        //app.getGraphics().
	}
}
