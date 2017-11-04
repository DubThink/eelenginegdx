package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontKit {
    static BitmapFont UtilSmall,UtilLarge,UtilHuge;
    public static void initFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        UtilSmall = generator.generateFont(parameter); // font size 12 pixels
        parameter.size = 24;
        UtilLarge = generator.generateFont(parameter); // font size 12 pixels
        parameter.size = 48;
        UtilHuge = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }
}
