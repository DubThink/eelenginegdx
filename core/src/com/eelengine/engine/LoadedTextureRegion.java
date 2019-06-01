package com.eelengine.engine;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.*;

public class LoadedTextureRegion extends TextureRegion implements Externalizable, Loadable{
    private static final long serialVersionUID = 3L;
    public static AssetSystem assetSystem;
    private static Texture nullTex= _makeNullTex();
    private static Texture _makeNullTex(){
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0xDEADBEFF);
        pix.fill();
        return new Texture(pix);
    }
    private static final int NONE=0;
    private static final int FULL=1;

    private transient int buildMode=NONE;
    public LoadedTextureRegion(){super();}
    public LoadedTextureRegion(String name) {
        super();
        buildMode=FULL;
        getTexFromFile(name);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(assetSystem.getAssetFileName(getTexture()));
        out.writeFloat(getU());
        out.writeFloat(getV());
        out.writeFloat(getU2());
        out.writeFloat(getV2());
        out.writeInt(getRegionWidth());
        out.writeInt(getRegionHeight());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        getTexFromFile(in.readUTF());
        setU(in.readFloat());
        setV(in.readFloat());
        setU2(in.readFloat());
        setV2(in.readFloat());
        setRegionWidth(in.readInt());
        setRegionHeight(in.readInt());
    }

    private void getTexFromFile(String file){
        if(assetSystem.isLoaded(file, Texture.class))
            build(file);
        else{
            assetSystem.queueAsset(file,Texture.class,this);
            setTexture(nullTex);
        }
    }
    @Override
    public void build(String asset) {
        if(buildMode==FULL){
            System.out.println("building "+asset);
            setRegion(assetSystem.get(asset,Texture.class));
        }else{
            setTexture(assetSystem.get(asset,Texture.class));

        }
    }
}
