package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Initializes and encapsulates fonts
 * @author Benjamin Welsh
 */
public class JamFontKit {
    public static BitmapFont SysSmall, SysMedium, SysLarge, SysHuge, SysMonolithic;
    public static void initFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Puritan-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.genMipMaps=true;
        parameter.size = 12;
        SysSmall = generator.generateFont(parameter);
        parameter.size = 18;
        SysMedium = generator.generateFont(parameter);
        parameter.size = 24;
        SysLarge = generator.generateFont(parameter);
        parameter.size = 48;
        SysHuge = generator.generateFont(parameter);
        parameter.size = 148;
        SysMonolithic = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }
    public static void dispose(){
        SysSmall.dispose();
        SysMedium.dispose();
        SysLarge.dispose();
        SysHuge.dispose();
        SysMonolithic.dispose();
    }
}
