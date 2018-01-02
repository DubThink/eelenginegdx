package com.eelengine.engine.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LevelSerializer {
    private FileHandle handle;
    private FileOutputStream fileOut;
    private ObjectOutputStream objOut;

    public LevelSerializer(FileHandle handle) {
        this.handle = handle;
        try {
            fileOut = new FileOutputStream(handle.file());
            objOut = new ObjectOutputStream(fileOut);
        } catch (IOException e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void serializeBrushes(ArrayList<Brush> brushes){
        try {
            for(Brush brush:brushes)
                objOut.writeObject(brush);
        }catch (IOException e){
            System.out.println(e);
        }
    }
    public void saveOut(){
        try{
        objOut.close();
        fileOut.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }
    void serialize(){

    }
}
