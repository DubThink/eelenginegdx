package com.eelengine.engine;

import bpw.UTester;
import bpw.Util;

/**
 * Represents an inventory of Items with a finite number of stacks
 */
public class Inventory {
    private int amounts[];
    private int size;
    /*The number of free stacks*/
    private int stacks;

    public Inventory(int size) {
        this.size=size;
        this.stacks=size;
        this.amounts = new int[Item.items.length];
    }

    /**
     * Returns the amount of a stored item
     * @param item the item to check
     * @return the amount stored
     */
    public int getAmount(Item item){
        return amounts[item.id];
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

    /**
     * Attempts to insert items into the inventory
     * @param item the item to insert
     * @param amt the amount to attempt to insert
     * @return the amount that was inserted
     */
    public int insert(Item item, int amt){
        if(amt<=0)return 0;
        amt= Util.min(amt,getSpace(item));
        int usage=getStackUsage(item);
        amounts[item.id]+=amt;
        stacks-=getStackUsage(item)-usage;
        return amt;
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
