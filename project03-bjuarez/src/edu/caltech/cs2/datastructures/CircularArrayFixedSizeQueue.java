package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {

    private E[] backingList;
    private int front;
    private int size;
//    private int back;

    public CircularArrayFixedSizeQueue(int capacity) {
        this.backingList = (E[]) new Object[capacity];
        this.front = capacity / 2;
        this.size = 0;
//        this.back = capacity / 2 - 1;
    }

    private int back() {
        int b = this.front + this.size() - 1;
        if (b >= this.capacity()) {
            return b - this.capacity();
        }
        return b;
    }

    @Override
    public String toString() {
        if (this.size() == 0) {
            return "[]";
        }
        String s = "[";

        int idx = this.back();
        for (int i = 0; i < this.size(); i++) {
            if (idx == 0) {
                idx = this.capacity() - 1;
            }
            s += this.backingList[idx] + ", ";
            idx--;
        }
        s = s.substring(0, s.length() - 2);
        return s + "]";
    }

    @Override
    public boolean isFull() {
        return this.capacity() == this.size();
    }

    @Override
    public int capacity() {
        return this.backingList.length;
    }

    @Override
    public boolean enqueue(E e) {
        // add to front
        if (this.isFull()) {
            return false;
        }
        if (this.front == 0) {
            this.front = this.capacity() - 1;
        }
        else {
            this.front = this.front - 1;
        }
        this.backingList[this.front] = e;
        this.size++;
        return true;
    }

    @Override
    public E dequeue() {
        // remove from back
        if (this.size() == 0) {
            return null;
        }
        E back = this.backingList[this.back()];
        this.backingList[this.back()] = null;
//        if (this.back() == 0) {
//            this.back() = this.capacity() - 1;
//        }
//        else {
//            this.back()--;
//        }
        this.size--;
        return back;
    }

    @Override
    public E peek() {
        // return from back
        if (this.size() == 0) {
            return null;
        }
        return this.backingList[this.back()];
    }

    @Override
    public int size() {
//        if (this.backingList[this.front] == null) {
//            return 0;
//        }
//        if (this.front > this.back()) {
//            return this.capacity() - this.front + this.back() + 1;
//        }
//        return this.back() - this.front + 1;
        return this.size;
    }

    private class CircularArrayFixedSizeQueueIterator implements Iterator<E> {

        private int start;
        private int count;

        public CircularArrayFixedSizeQueueIterator() {
            this.start = CircularArrayFixedSizeQueue.this.back();
            this.count = 0;
        }

        public boolean hasNext() {
            return count < CircularArrayFixedSizeQueue.this.size();
        }

        public E next() {
            if (this.start < 0) {
                this.start = CircularArrayFixedSizeQueue.this.capacity() - 1;
            }
            E e = CircularArrayFixedSizeQueue.this.backingList[this.start];
            this.start--;
            this.count++;
            return e;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularArrayFixedSizeQueueIterator();
    }
}
