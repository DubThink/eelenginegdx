package com.eelengine.engine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.eelengine.engine.LoadedTextureRegion;
import com.eelengine.engine.StaticSprite;

import java.io.*;

public class LevelIO {
    public static void saveLevelSource(FileHandle handle, LevelSource source){
        saveObject(handle,source);

    }
    public static LevelSource loadLevelSource(FileHandle handle){
        try {
            FileInputStream fileIn = new FileInputStream(handle.file());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                return (LevelSource) in.readObject();
            } catch (ClassNotFoundException c) {
                System.err.println("LevelSource file is not valid: "+c);
            }
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.err.println("Unable to load LevelSource file: "+i);
        }
        return null;
    }

    public static void saveLoadedTextureRegion(FileHandle handle, LoadedTextureRegion region){
        saveObject(handle,region);
    }
    public static void saveObject(FileHandle handle, Serializable region){
        try {
            FileOutputStream fileOut = new FileOutputStream(handle.file());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(region);
            out.close();
            fileOut.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public static LoadedTextureRegion loadLoadedTextureRegion(FileHandle handle){
        try {
            FileInputStream fileIn = new FileInputStream(handle.file());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                return (LoadedTextureRegion) in.readObject();
            } catch (ClassNotFoundException c) {
                System.err.println("LevelSource file is not valid: "+c);
            }
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.err.println("Unable to load LevelSource file: "+i);
        }
        return null;
    }
    public static StaticSprite loadStaticSprite(FileHandle handle){
        try {
            FileInputStream fileIn = new FileInputStream(handle.file());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                return (StaticSprite) in.readObject();
            } catch (ClassNotFoundException c) {
                System.err.println("LevelSource file is not valid: "+c);
            }
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.err.println("Unable to load LevelSource file: "+i);
        }
        return null;
    }

}
