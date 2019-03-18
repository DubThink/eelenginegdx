package com.eelengine.engine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import jdk.nashorn.internal.parser.JSONParser;

import java.util.HashMap;

public class Recipe {

    private ItemCollection inputs;
    private ItemCollection outputs;

    /**
     * Checks if @link{ItemCollection} c contains the inputs for the recipe
     * @return true if all inputs are present
     */
    public boolean checkInputs(ItemCollection c){
        return inputs.subsetOf(c);
    }

    /**
     * Removes the inputs for the recipe from c
     */
    public void useInputs(MutableItemCollection c){
        assert checkInputs(c); /* this should be pre-checked by the caller */
        c.subtract(inputs);
    }

    public ItemCollection getOutputs(){
        return outputs;
    }

    private Recipe(ItemCollection inputs, ItemCollection outputs){
        this.inputs=inputs;
        this.outputs=outputs;
    }

    // -- recipe collection stuff -- //

    private static HashMap<String,Recipe> recipesByName=new HashMap<>();

    static void loadRecipes(FileHandle file){
        ItemCollectionFactory inputFactory=new ItemCollectionFactory();
        ItemCollectionFactory outputFactory=new ItemCollectionFactory();

        JsonReader json = new JsonReader();
        JsonValue base = json.parse(file);


        for(JsonValue component : base){
            for(JsonValue item:component.get("Inputs")){
                inputFactory.set(Item.valueOf(item.name),item.asInt());
            }
            for(JsonValue item:component.get("Outputs")){
                outputFactory.set(Item.valueOf(item.name),item.asInt());
            }
            Recipe n=new Recipe(inputFactory.yeild(),outputFactory.yeild());
            recipesByName.put(component.name,n);
        }
    }

    public static boolean hasRecipe(String name){
        return recipesByName.containsKey(name);
    }
    public static Recipe getRecipe(String name){
        assert hasRecipe(name);
        return recipesByName.get(name);
    }

}
