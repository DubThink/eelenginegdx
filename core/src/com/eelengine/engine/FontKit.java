package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Initializes and encapsulates fonts
 * @author Benjamin Welsh
 */
public class FontKit {
    static BitmapFont SysSmall, SysLarge, SysHuge;
    public static void initFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.genMipMaps=true;
        parameter.size = 12;
        SysSmall = generator.generateFont(parameter);
        parameter.size = 24;
        SysLarge = generator.generateFont(parameter);
        parameter.size = 48;
        SysHuge = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }
    public void dispose(){
        SysSmall.dispose();
        SysLarge.dispose();
        SysHuge.dispose();
    }
}
