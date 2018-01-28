package com.eelengine.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MailGame extends EelGame {
    LoadedTextureRegion moneyIcon;
    @Override
    public void gameCreate() {
        JamFontKit.initFonts();
        moneyIcon=new LoadedTextureRegion("sprites/interface/dollas.png");
    }

    @Override
    public void renderUI() {
        float width=Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        interfaceBatch.begin();
        GlyphLayout layout = new GlyphLayout(JamFontKit.SysMonolithic,"GAEM");
        JamFontKit.SysMonolithic.draw(interfaceBatch,"GAEM", Gdx.graphics.getWidth()/2-layout.width/2,Gdx.graphics.getHeight()/2+layout.height/2);
        interfaceBatch.draw(moneyIcon,0,0);

        interfaceBatch.end();
        super.renderUI();
    }

    @Override
    public void dispose() {
        super.dispose();
        JamFontKit.dispose();
    }
}
