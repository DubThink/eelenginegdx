package com.eelengine.engine.sprite;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class PeekableIterator<T> {
    private Iterator<T> iterator;
    private T next;
    public PeekableIterator(Iterator<T> iterator) {
        this.iterator = iterator;
        next=getNext();
    }

    public boolean hasNext() {
        return next!=null;
    }

    public T peekNext(){
        return next;
    }

    public T next(){
        T tmp = next;
        next=getNext();
        return tmp;
    }
    private T getNext(){
        if(iterator.hasNext())return iterator.next();
        return null;
    }

    void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
