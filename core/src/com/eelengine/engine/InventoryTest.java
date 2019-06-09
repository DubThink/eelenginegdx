package com.eelengine.engine;


import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void getSpace() {
        Inventory i = new Inventory(10);
        assert i.getSpace(Item.COAL)==Item.COAL.getStackSize()*10;
        i.insert(Item.COAL,5);
        assert i.getSpace(Item.COAL)==Item.COAL.getStackSize()*10 - 5;
        i.insert(Item.IRON,1);
        assert i.getSpace(Item.COAL)==Item.COAL.getStackSize()*9 - 5;
        i.insert(Item.COAL,Item.COAL.getStackSize());
        assert i.getSpace(Item.COAL)==Item.COAL.getStackSize()*8 - 5;
    }

    @Test
    void insert() {
        Inventory i = new Inventory(10);
        assert i.insert(Item.COAL,-10)==0;
        assert i.getAmount(Item.COAL)==0;
        assert i.insert(Item.COAL,0)==0;
        assert i.getAmount(Item.COAL)==0;
        assert i.insert(Item.COAL,5)==5;
        assert i.getAmount(Item.COAL)==5;

    }

    @Test
    void remove() {
    }

    @Test
    void setAmount() {
    }

    @Test
    void getSize() {
    }

    @Test
    void getFreeStacks() {
    }
}