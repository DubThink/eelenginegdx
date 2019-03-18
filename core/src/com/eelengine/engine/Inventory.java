package com.eelengine.engine;

import bpw.UTester;
import bpw.Util;

/**
 * Represents an inventory of Items with a finite number of stacks
 */
public class Inventory extends MutableItemCollection {
    private int size;
    /*The number of free stacks*/
    private int stacks;

    public Inventory(int size) {
        super();
        this.size=size;
        this.stacks=size;
    }

    /**
     * Returns the amount of available space for an item
     * @param item item to check
     * @return number of items that could be inserted
     */
    public int getSpace(Item item){
        int stackSize=item.getStackSize();
        int partStack=(stackSize-amounts[item.id]%stackSize)%stackSize;
        return stacks*stackSize + partStack;

    }

    /**Returns the number of stacks of an item, including partial stacks*/
    private int getStackUsage(Item item){
        return (int)Math.ceil(amounts[item.id]/(float)item.getStackSize());
    }

    private void modifyItemCount(Item item,int amt){
        int usage=getStackUsage(item);
        amounts[item.id]+=amt;
        stacks-=getStackUsage(item)-usage;
    }

    /**
     * Attempts to insert items into the inventory
     * @param item the item to insert
     * @param amt the amount to attempt to insert
     * @return the amount that was inserted
     */
    public int insert(Item item, int amt){
        if(amt<=0)return 0;
        amt= Util.min(amt,getSpace(item));
        modifyItemCount(item,amt);
        return amt;
    }

    /**
     * Attempts to insert items into the inventory
     * @return the amount that was inserted
     */
    public ItemCollection insert(ItemCollection c){
        int overflow[] = new int[Item.items.length];
        for(Item i:Item.items){
            overflow[i.id]=insert(i,c.amounts[i.id]);
        }
        return new ItemCollection(overflow);
    }

    /**
     * Attempts to remove items from the inventory
     * @param item the item to remove
     * @param amt the amount to attempt to remove
     * @return the amount that was removed
     */
    public int remove(Item item, int amt){
        if(amt<=0)return 0;
        amt = Util.min(amt,getAmount(item));
        modifyItemCount(item,-amt);
        return amt;
    }

    @Override
    public void setAmount(Item item, int amt) {
        if(amt<0)throw new ValueError();
        modifyItemCount(item,amt-getAmount(item));
        if(amt!=getAmount(item))throw new ValueError();
    }

    @Override
    public void subtract(ItemCollection c) {
        assert supersetOf(c); /* this should be pre-checked by the caller */
        for(Item item:Item.items)
            remove(item,c.amounts[item.id]);
    }

    /**
     * Gets the size in stacks
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the number of free stacks
     */
    public int getFreeStacks() {
        return stacks;
    }

    public static void main(String[] args) {
        Inventory i = new Inventory(10);
        UTester tester=new UTester();
        tester.start("Inventory");

        tester.testInt(i.getSpace(Item.COAL),Item.COAL.getStackSize()*10);
        tester.testInt(i.getAmount(Item.COAL),0);
        tester.testInt(i.insert(Item.COAL,-10),0);
        tester.testInt(i.getAmount(Item.COAL),0);

        tester.testInt(i.insert(Item.COAL,0),0);
        tester.testInt(i.getAmount(Item.COAL),0);
        tester.testInt(i.insert(Item.COAL,5),5);
        tester.testInt(i.getAmount(Item.COAL),5);

        tester.testInt(i.insert(Item.COAL,19),19);
        tester.testInt(i.getAmount(Item.COAL),24);
        tester.testInt(i.getSpace(Item.COAL),176);
        tester.testInt(i.getSpace(Item.IRON_ORE),80);

        tester.testInt(i.getStackUsage(Item.COAL),2);
        tester.testInt(i.insert(Item.COAL,10101),176);
        tester.testInt(i.getSpace(Item.IRON_ORE),0);
        tester.testInt(i.getAmount(Item.COAL),200);
        tester.end();
    }
}
