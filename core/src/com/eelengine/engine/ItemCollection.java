package com.eelengine.engine;

public class ItemCollection {
    protected int amounts[];

    public ItemCollection() {
        this.amounts = new int[Item.items.length];
    }

    public ItemCollection(int[] amounts) {
        assert amounts.length==Item.items.length;
        this.amounts = amounts.clone();
    }

    public ItemCollection(ItemCollection c){
        this(c.amounts);
    }

    /**
     * Returns the amount of a stored item
     * @param item the item to check
     * @return the amount stored
     */
    public int getAmount(Item item){
        return amounts[item.id];
    }

    public boolean subsetOf(ItemCollection c){
        for(int i=0;i<Item.items.length;i++) {
            if (this.amounts[i] > c.amounts[i])
                return false;
        }
        return true;
    }

    public boolean supersetOf(ItemCollection c){
        for(int i=0;i<Item.items.length;i++) {
            if (this.amounts[i] < c.amounts[i])
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s= new StringBuilder("ItemCollection(");
        for(Item item:Item.values()){
            if(getAmount(item)>0) s.append(item.name()).append(":").append(getAmount(item)).append(",");
        }
        return s+")";
    }
}
