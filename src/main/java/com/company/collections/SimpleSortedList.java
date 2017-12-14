package com.company.collections;

import com.company.collections.contracts.SimpleOrderedBag;
import com.company.staticData.ExceptionMessages;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class SimpleSortedList<T extends Comparable<T>> implements SimpleOrderedBag<T> {
    private static final int DEFAULT_SIZE = 16;
    private T[] innerCollection;
    private Comparator<T> comparator;
    private int size;

    public SimpleSortedList(Class<T> type, Comparator<T> comparator, int capacity) {
        this.initializeInnerCollection(type, capacity);
        this.comparator = comparator;
        this.size = 0;
    }

    public SimpleSortedList(Class<T> type, Comparator<T> comparator) {
        this(type, comparator, DEFAULT_SIZE);
    }

    public SimpleSortedList(Class<T> type, int capacity) {
        this(type, Comparable::compareTo, capacity);
    }

    public SimpleSortedList(Class<T> type) {
        this(type, Comparable::compareTo, DEFAULT_SIZE);
    }

    @Override
    public boolean remove(T element) {
        if (element == null){
            throw new IllegalArgumentException();
        }
        boolean hasBeenRemoved = false;
        int indexOfRemovedElement = 0;

        for (int i = 0; i < this.size(); i++) {
            if (this.innerCollection[i].compareTo(element) == 0){
                indexOfRemovedElement = i;
                this.innerCollection[i] = null;
                hasBeenRemoved = true;
                break;
            }
        }

        if (hasBeenRemoved){
            for (int i = indexOfRemovedElement; i < this.size() - 1; i++) {
                this.innerCollection[i] = this.innerCollection[i + 1];
            }
            this.innerCollection[this.size() - 1] = null;
            this.size--;
        }
        return hasBeenRemoved;
    }

    @Override
    public int capacity() {
        return this.innerCollection.length;
    }

    @Override
    public void add(T element) {
        if (element == null){
            throw new IllegalArgumentException();
        }
        if (this.size == this.innerCollection.length){
            this.resize();
        }

        this.innerCollection[this.size++] = element;
        Arrays.sort(this.innerCollection, 0, this.size, this.comparator);
    }

    @Override
    public void addAll(Collection<T> elements) {
        if (elements == null){
            throw new IllegalArgumentException();
        }
        if (this.size + elements.size() >= this.innerCollection.length){
            this.multiResize(elements);
        }

        for (T element : elements) {
            this.innerCollection[this.size++] = element;
        }

        Arrays.sort(this.innerCollection, 0, this.size, this.comparator);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String joinWith(String joiner) {
        if (joiner == null){
            throw new IllegalArgumentException();
        }
        StringBuilder output = new StringBuilder();
        for (T element : this) {
            output.append(element).append(joiner);
        }

        output.setLength(output.length() - joiner.length());
        return output.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return this.index < size;
            }

            @Override
            public T next() {
                return innerCollection[index++];
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void initializeInnerCollection(Class<T> type, int capacity) {
        if (capacity < 0 ){
            throw new IllegalArgumentException(ExceptionMessages.NEGATIVE_CAPACITY);
        }

        this.innerCollection = (T[]) Array.newInstance(type, capacity);
    }

    private void resize() {
        this.innerCollection = Arrays.copyOf(this.innerCollection, this.innerCollection.length * 2);
    }

    private void multiResize(Collection<T> elements) {
        int newSize = this.innerCollection.length * 2;
        while (this.size + elements.size() >= newSize){
            newSize *= 2;
        }

        this.innerCollection = Arrays.copyOf(this.innerCollection, newSize);
    }
}
