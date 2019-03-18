package com.eelengine.engine;

public class ItemCollectionFactory {
    MutableItemCollection itemCollection;

    public ItemCollectionFactory() {
        itemCollection=new MutableItemCollection();
    }

    public ItemCollection yeild(){
        ItemCollection out=new ItemCollection(itemCollection);
        itemCollection=new MutableItemCollection();
        return out;
    }

    public void set(Item i, int amt){
        itemCollection.setAmount(i,amt);
    }
}
