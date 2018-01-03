package com.eelengine.engine.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;

public class LevelIO {
    public static void saveLevelSource(FileHandle handle, LevelSource source){
        try {
            FileOutputStream fileOut = new FileOutputStream(handle.file());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(source);
            out.close();
            fileOut.close();
        }catch (IOException e){
            System.out.println(e);
        }
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
}
