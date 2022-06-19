package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    // Appropriately swaps data between PQElements and updates keyToIndexMap
    private void swap_data(int idx1, int idx2) {
        PQElement<E> curr = this.data[idx1]; // temp variable
        this.data[idx1] = this.data[idx2]; // swaps data
        this.data[idx2] = curr; // puts temp data in appropriate slot
        this.keyToIndexMap.put(this.data[idx1].data, idx1); // updates appropriate mapping
        this.keyToIndexMap.put(this.data[idx2].data, idx2); // updates appropriate mapping
    }

    private void percolateDown(int index) {
        if (this.size == 0) {
            return; // does nothing if heap is empty
        }
        int child = (4 * index) + 1; // index of child
        int current = index; // current index
        int smaller_child = -1; // used to store index of smallest child
        for (int i = child; i < child + 4; i++) { // simulates looping through childs
            if (i >= this.size) { // keeps track of appropriate indices
                break;
            }
            if (smaller_child == -1) { // checks initial case
                smaller_child = i;
            }
            if (this.data[i].priority < this.data[smaller_child].priority) { // compares children
                smaller_child = i; // updates index of smallest child
            }
        }
        if (smaller_child == -1) { // handles index out of bounds error
            return;
        }
        // compares current element with smallest child
        while (this.data[smaller_child].priority < this.data[current].priority) {
            this.swap_data(current, smaller_child); // swaps data appropriately
            if (4 * smaller_child+ 1 >= this.size) { // checks if more children can be found
                break;
            }
            current = smaller_child; // moves down to smallest child
            child = 4 * current + 1; // finds next children
            for (int i = child; i < child + 4; i++) { // loops through children
                if (i >= this.size) {
                    continue;
                }
                if (this.data[i].priority < this.data[smaller_child].priority) { // finds smallest child
                    smaller_child = i; // updates index of smallest child
                }
            }
        }
        if (this.keyToIndexMap.get(this.data[0].data) != 0) { // checks that index in keyToIndexMap is accurate
            this.keyToIndexMap.put(this.data[0].data, 0); // updates if needed
        }
    }

    private void percolateUp(int index) {
        if (this.size == 0) {
            return; // does nothing if heap is empty
        }
        int parent = (index - 1) / 4; // calculates index of parent
        int current = index; // stores current index
        // compares current element with parent
        while (this.data[current].priority < this.data[parent].priority) {
            this.swap_data(current, parent); // swaps data appropriately
            if (parent == 0) { // checks if more parents are available
                break;
            }
            current = parent; // starts at previous parent
            parent = (current - 1) / 4; // finds next parent
        }
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if (!this.keyToIndexMap.containsKey(key.data)) { // checks if key.data is present
            throw new IllegalArgumentException("key.data is not an element in the queue");
        }
        else {
            this.data[this.keyToIndexMap.get(key.data)] = key; // updates appropriate element
            this.percolateDown(this.keyToIndexMap.get(key.data)); // updates placement of element with new priority
        }
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        if (!this.keyToIndexMap.containsKey(key.data)) { // checks if key.data is present
            throw new IllegalArgumentException("key.data is not an element in the queue");
        }
        else {
            this.data[this.keyToIndexMap.get(key.data)] = key; // updates appropriate element
            this.percolateUp(this.keyToIndexMap.get(key.data)); // updates placement of element with new priority
        }
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if (this.keyToIndexMap.containsKey(epqElement.data)) {
            throw new IllegalArgumentException("epqElement.data is already an element in the queue");
        }
        if (this.size == this.data.length) { // checks if heap is full, increases capacity if necessary
            PQElement<E>[] new_heap = new PQElement[2 * this.size]; // new heap to replace this.data (doubles size)
            for (int i = 0; i < this.size; i++) {
                new_heap[i] = this.data[i]; // copies data into new heap
            }
            this.data = new_heap; // sets this.data to new_heap
        }
        this.data[this.size] = epqElement; // places new element on "bottom"
        this.keyToIndexMap.put(epqElement.data, this.size); // updates keyToIndexMap initially
        this.percolateUp(this.size); // updates placement of new element according to its priority
        this.size++; // increases size appropriately
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (this.size == 0) { // checks if heap is empty
            return null;
        }
        PQElement<E> to_dequeue = this.data[0]; // holds element to dequeue from front
        this.keyToIndexMap.remove(this.data[0].data); // removes mapping appropriately
        this.data[0] = this.data[this.size - 1]; // places "bottom" element at top of heap
        this.data[this.size - 1] = null; // replaces slot with null appropriately
        this.size--; // decrements size before percolating
        this.percolateDown(0); // updates placement of element which was previously at the "bottom"
        return to_dequeue; // returns removed element
    }

    @Override
    public PQElement<E> peek() {
        if (this.size == 0) { // checks if heap is empty
            return null;
        }
        return this.data[0]; // returns element on top of heap
    }

    @Override
    public int size() {
        return this.size; // returns appropriate size
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }
}