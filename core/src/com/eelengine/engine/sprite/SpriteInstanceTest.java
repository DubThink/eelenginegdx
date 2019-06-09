package com.eelengine.engine.sprite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpriteInstanceTest {
    @Test
    public void test1(){
        SpriteInstance instance1 = new SpriteInstance(null,0,0,0);
        SpriteInstance instance2 = new SpriteInstance(null,0,0,0);
        assertEquals(0,SpriteInstance.renderOrderComparator.compare(instance1,instance2));
        instance1.layer=1;
        assertEquals(1,SpriteInstance.renderOrderComparator.compare(instance1,instance2));
        instance1.layer=0;
        instance1.y=-1;
        assertEquals(1,SpriteInstance.renderOrderComparator.compare(instance1,instance2));
        instance1.y=0;
        instance1.x=1;
        assertEquals(1,SpriteInstance.renderOrderComparator.compare(instance1,instance2));

    }
}