package com.eelengine.engine;

import bpw.Tuple;
import com.badlogic.gdx.assets.AssetManager;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AssetSystem extends AssetManager{
    public static boolean debug=false;
    private Queue<Tuple<String,Loadable>> loadables=new ConcurrentLinkedDeque<>();

    public AssetSystem() {
        super();
    }
    public void queueAsset(String asset, Class assetType, Loadable loadable){
        if(isLoaded(asset,assetType))loadable.build(asset);
        else {
            loadables.add(new Tuple<>(asset,loadable));
            load(asset,assetType);
        }
    }

    @Override
    public boolean update() {
        boolean status = super.update();
        if(status&&loadables.size()>0){
            if(debug)System.out.println("Loaded all assets, building "+loadables.size()+" loadables...");
//            if(debug)System.out.println(this.getProgress());

            for (Tuple<String,Loadable> loadable; (loadable = loadables.poll()) != null;){
                loadable.y.build(loadable.x);
            }
        }
        return status;
    }
}
