package com.eelengine.engine;

/**
 * Defines items and their properties
 * @author Benjamin Welsh
 */
public enum Item {
    IRON_ORE ("Iron ore",10),
    COPPER_ORE ("Copper ore",10),
    COAL ("Coal",20),
    STONE ("Stone",20),
    IRON("Iron bar",10);

    private String hrName;
    final int id;
    private int stackSize;   // in kilograms
    private short bits; // in meters
    private Item smeltProduct;

    static {
        IRON_ORE.smeltProduct=IRON;
        IRON_ORE.bits=ItemProp.SMELTABLE;
    }

    Item(String hrName, int stackSize){
        this(hrName, stackSize,ItemProp.NONE);
    }

    Item(String hrName, int stackSize,short bits) {
        this.id=this.ordinal();
        this.stackSize = stackSize;
        this.bits = bits;
        this.hrName=hrName;
        this.smeltProduct=null;
    }

    public String getHrName() {
        return hrName;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Item getSmeltProduct() {
        return smeltProduct;
    }

    public boolean testFlag(short flag){
        return (this.bits|flag)!=0;
    }
    public static final Item items[] = Item.values();
}
