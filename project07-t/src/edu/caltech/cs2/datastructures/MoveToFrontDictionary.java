package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private int size; // size of dictionary
    private DictNode<K, V> head; // represents front of dictionary

    private static class DictNode<K, V> { // necessary for keeping track of item at front of list
        public K key;
        public V value;
        public DictNode<K, V> next; // keeps track of next node in dictionary

        public DictNode(K key, V value, DictNode<K, V> node) {
            this.key = key;
            this.value = value;
            this.next = node;
        }
    }

    public MoveToFrontDictionary() {
        // initializes empty dictionary
        this.size = 0;
        this.head = null;
    }

    @Override
    public V remove(K key) {
        if (this.size() == 0 || this.head == null) {
            return null; // returns null if dictionary if empty
        }
        V value_to_return; // value of removed node
        if (this.head.key.equals(key)) { // checks if head node contains inputted key
            // removes current head node and makes next node the new head node
            value_to_return = this.head.value;
            this.head = this.head.next;
        }
        else { // recursively checks next nodes in dictionary if head node isn't removed
            value_to_return = remove_helper(key, this.head);
            if (value_to_return == null) { // is null if no nodes need to be removed
                return null;
            }
        }
        this.size--; // accounts for removal of node
        return value_to_return; // returns value of removed node
    }

    private V remove_helper(K key, DictNode<K, V> node) {
        if (node.next == null) {
            return null; // implies that end of dictionary is reached and key is not found
        }
        if (node.next.key.equals(key)) { // checks if next node has inputted key
            V value_to_return = node.next.value; // stores value of node to be removed
            node.next = node.next.next; // shifts nodes appropriately
            return value_to_return; // returns value of removed node
        }
        else { // moves onto next nodes
            return remove_helper(key, node.next); // recursively calls helper to check next nodes
        }
    }

    @Override
    public V put(K key, V value) {
        V value_to_return;
        if (this.containsKey(key)) { // checks if key already exists in dictionary
            value_to_return = this.get(key); // stores value from key and moves node to front
            this.head.value = value; // sets new head value to inputted value
            return value_to_return; // returns old value
        }
        else { // handles case when no previous mapping exists
            if (this.size() == 0 || this.head == null) { // checks size
                this.head = new DictNode<>(key, value, null); // creates new head
            }
            else {
                DictNode<K, V> old_head = this.head; // stores old head
                this.head = new DictNode<>(key, value, old_head); // puts new node at front with old head node as next
            }
            this.size++;
            return null;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>(); // to store keys
        if (this.size() == 0) { // checks if dictionary is empty
            return keys;
        }
        return keys_helper(keys, this.head); // otherwise, recursively calls helper to check nodes
    }

    private ICollection<K> keys_helper(ICollection<K> keys, DictNode<K, V> node) {
        if (node == null) { // checks if at end of dictionary (BASE CASE)
            return keys;
        }
        keys.add(node.key); // adds key to keys
        return keys_helper(keys, node.next); // recursively calls helper to check next node
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> values = new LinkedDeque<>(); // to store values
        if (this.size() == 0) { // checks if dictionary is empty
            return values;
        }
        return values_helper(values, this.head); // otherwise, recursively calls helper to check nodes
    }

    private ICollection<V> values_helper(ICollection<V> values, DictNode<K, V> node) {
        if (node == null) { // checks if at end of dictionary (BASE CASE)
            return values;
        }
        values.add(node.value); // adds value to values
        return values_helper(values, node.next); // recursively calls helper to check next node
    }


    public V get(K key) {
        if (this.size == 0) { // checks if dictionary is empty
            return null;
        }
        if (this.head.key.equals(key)) { // checks if head node has key
            return head.value; // returns value at front (no need to move anything to front)
        }
        DictNode<K, V> previous_node = this.head; // front of dictionary
        DictNode<K, V> node = this.head.next; // next node from front
        for (int i = 0; i < this.size(); i++) { // loops through size of dictionary times
            if (node != null) { // handles end case
                if (node.key.equals(key)) { // checks if key at node is same as inputted key
                    previous_node.next = node.next; // "shifts" nodes appropriately
                    node.next = this.head; // puts old head node as next node from node that is being put at front
                    this.head = node; // puts referenced node at the front
                    return this.head.value; // returns the value of the new head node
                }
                // otherwise, moves to the next nodes
                previous_node = node;
                node = node.next;
            }
        }
        return null; // returns null if key is never found
    }

    @Override
    public Iterator<K> iterator() {
        return new DictIterator();
    }

    private class DictIterator implements Iterator<K> {
        private DictNode<K, V> dict_node = MoveToFrontDictionary.this.head; // starts at front of dictionary

        @Override
        public boolean hasNext() {
            return this.dict_node != null; // checks if node is null
        }

        public K next() {
            if (!this.hasNext()) {
                return null; // returns null if there doesn't exist a next node
            }
            K key_to_return = this.dict_node.key; // stores key of node to return
            if (this.dict_node.next == null) { // checks if next node is null
                this.dict_node = null;
            }
            else {
                this.dict_node = this.dict_node.next; // otherwise, goes to next node
            }
            return key_to_return;
        }
    }
}
