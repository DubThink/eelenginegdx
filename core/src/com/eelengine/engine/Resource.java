package com.eelengine.engine;

public enum Resource {
    IRON(Item.IRON_ORE),
    COPPER(Item.COPPER_ORE),
    DIRT(Item.DIRT),
    SAND(Item.SAND),
    ROCK(Item.STONE);

    Item yields;

    Resource(Item yields) {
        this.yields = yields;
    }
}
