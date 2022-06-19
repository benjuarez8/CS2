package edu.caltech.cs2.sorts;

import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IPriorityQueue;

public class TopKSort {
    /**
     * Sorts the largest K elements in the array in descending order. Modifies the array in place.
     * @param array - the array to be sorted; will be manipulated.
     * @param K - the number of values to sort
     * @param <E> - the type of values in the array
     * @throws IllegalArgumentException if K < 0
     */
    public static <E> void sort(IPriorityQueue.PQElement<E>[] array, int K) {
        if (K < 0) { // checks appreciate value of K
            throw new IllegalArgumentException("K cannot be negative!");
        }
        MinFourHeap<E> topKheap = new MinFourHeap<>(); // heap to hold top k items by priorities
        for (IPriorityQueue.PQElement<E> p : array) { // loops through elements in array
            topKheap.enqueue(p); // adds element to the heap (and sorts)
            if (topKheap.size() > K) { // checks if over K elements
                topKheap.dequeue(); // dequeues appropriately
            }
        }
        for (int i = array.length - 1; i >= 0; i--) { // goes through each slot in array
            if (i >= K) { // checks if index if greater than or equal to K
                array[i] = null; // sets to null if so
            }
            else {
                array[i] = topKheap.dequeue(); // sets the first k slots to the top k items
            }
        }
    }
}
