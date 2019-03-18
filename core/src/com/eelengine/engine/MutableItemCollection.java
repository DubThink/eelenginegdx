package com.eelengine.engine;

public class MutableItemCollection extends ItemCollection {
    public void subtract(ItemCollection c){
        assert supersetOf(c); /* this should be pre-checked by the caller */
        for(int i=0;i<Item.items.length;i++)
            this.amounts[i] -= c.amounts[i];
    }

    public void setAmount(Item item, int amt){
        this.amounts[item.id]=amt;
    }
    public class ValueError extends Error{}
}
