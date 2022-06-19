package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private E[] data;
    private int size;
    private final static int DEFAULT_CAPACITY = 10; // check
    private final static int INITIAL_SIZE = 0; // check

    public ArrayDeque(int initialCapacity) {
        this.data = (E[])new Object[initialCapacity];
        this.size = INITIAL_SIZE;
    }

    public ArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        String s = "[";
        for (int i = 0; i < this.size; i++) {
            s += this.data[i] + ", ";
        }
        s = s.substring(0, s.length() - 2);
        return s + "]";
    }

    // CHECK!!!
    private void resize() {
        if (this.size == this.data.length) {
            E[] newData = (E[])new Object[this.data.length * 2];
            for (int i = 0; i < this.size; i++) {
                newData[i] = this.data[i];
            }
            this.data = newData;
        }
    }

    @Override
    public void addFront(E e) {
        resize();
        for (int i = this.size; i > 0; i--) {
            this.data[i] = this.data[i-1];
        }
        this.data[0] = e;
        this.size++;
    }

    @Override
    public void addBack(E e) {
        resize();
        this.data[this.size] = e;
        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.size == 0) {
            return null;
        }
        E front = this.data[0];
        for (int i = 0; i < this.size - 1; i++) {
            this.data[i] = this.data[i+1];
        }
        this.size--;
        return front;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) {
            return null;
        }
        E back = this.data[this.size - 1];
        this.size--;
        // set to null?
        return back;
    }

    @Override
    public boolean enqueue(E e) {
        // adds element to "front"
        this.addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        // removes element from "back"
        return this.removeBack();
    }

    @Override
    public boolean push(E e) {
        // adds element to "back"
        this.addBack(e);
        return true;
    }

    @Override
    public E pop() {
        // removes element from "back"
        return this.removeBack();
    }

    @Override
    public E peek() {
        // returns element from "back"
        if (this.size == 0){
            return null;
        }
        return this.data[this.size - 1];
    }

    @Override
    public E peekFront() {
        // returns element from "front"
        if (this.size == 0){
            return null;
        }
        return this.data[0];
    }

    @Override
    public E peekBack() {
        // returns element from "back"
        if (this.size == 0){
            return null;
        }
        return this.data[this.size - 1];
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<E> {

        private int currentIdx;

        public ArrayDequeIterator() {
            this.currentIdx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.currentIdx < (ArrayDeque.this).size();
        }

        @Override
        public E next() {
            E e = ArrayDeque.this.data[currentIdx];
            this.currentIdx++;
            return e;
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}

