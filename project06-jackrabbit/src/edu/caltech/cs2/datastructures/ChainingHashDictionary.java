package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private int size;
    private Object[] buckets;
    private final int[] CAPACITIES = {11, 31, 73, 151, 353, 809, 1657, 3359, 7001, 14503, 30011, 65003, 135007,
            275003, 550007};
    private int cap_idx = 0;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.chain = chain;
        this.buckets = new Object[this.CAPACITIES[0]]; // some prime number
    }

    private int keyToIndex(K key) {
        int i = key.hashCode() % this.CAPACITIES[this.cap_idx];
        if (i < 0) {
            return i * -1;
        }
        return i; // generates index from key
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        IDictionary<K, V> dict = (IDictionary<K, V>) buckets[this.keyToIndex(key)]; // casts bucket as dictionary
        if (dict == null) { // checks if bucket slot is null
            return null;
        }
        return dict.get(key); // returns appropriate value within bucket
    }

    @Override
    public V remove(K key) {
        IDictionary<K, V> dict = (IDictionary<K, V>) buckets[this.keyToIndex(key)]; // casts bucket as dictionary
        if (dict == null) { // checks if bucket slot is null
            return null;
        }
        if (!dict.containsKey(key)) { // checks if key is in bucket
            return null;
        }
        this.size--; // updates size
        V value_to_return = this.get(key); // stores value mapped by key
        dict.remove(key); // removes key
        return value_to_return;
    }

    private void resize_buckets() {
        this.cap_idx++; // calculates new capacity
        int new_capacity = this.CAPACITIES[cap_idx]; // generates new capacity
        Object[] temp = new Object[new_capacity];
        IDictionary<K, V> old_dict; // stores dictionaries from buckets
        IDictionary<K, V> dict; // stores dictionaries for new buckets
        for (Object d : this.buckets) { // loops through each "bucket"
            if (d != null) {
                old_dict = (IDictionary<K, V>) d; // casts as dictionary
                for (K k : old_dict.keys()) {
                    if (temp[this.keyToIndex(k)] == null) {
                        temp[this.keyToIndex(k)] = chain.get(); // creates chain if index is empty
                    }
                    dict = (IDictionary<K, V>) temp[this.keyToIndex(k)]; // casts as dictionary
                    dict.put(k, old_dict.get(k)); // puts correct mappings in new dictionary
                }
            }
        }
        this.buckets = temp; // replace old buckets
    }

    @Override
    public V put(K key, V value) {
        // load factor ~ 1
        if (this.size/(double)this.CAPACITIES[this.cap_idx] > 1) { // checks load factor
            // RESIZING
            this.resize_buckets();
        }
        int index = this.keyToIndex(key); // stores index from key
        if (this.buckets[index] == null) {
            this.buckets[index] = chain.get(); // creates chain if index is empty
        }
        IDictionary<K, V> dict = (IDictionary<K, V>) buckets[index]; // casts bucket as dictionary
        V value_to_return = dict.get(key); // stores appropriate value to return
        if (value_to_return == null) {
            this.size++; // updates size of buckets
        }
        dict.put(key, value); // puts new key/value pair in bucket
        return value_to_return; // returns old value
    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>(); // stores keys
        IDictionary<K, V> dict; // dictionary within each bucket
        for (Object d : this.buckets) { // loops through buckets
            if (d != null) { // checks if bucket is empty
                dict = (IDictionary<K, V>) d; // creates dictionary
                for (K k : dict.keys()) { // loops through keys in dictionary
                    keys.add(k); // adds key to keys
                }
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> values = new LinkedDeque<>(); // stores values
        IDictionary<K, V> dict; // dictionary within each bucket
        for (Object d : this.buckets) { // loops through buckets
            if (d != null) { // checks if bucket is empty
                dict = (IDictionary<K, V>) d; // creates dictionary
                for (V v : dict.values()) { // loops through values in dictionary
                    values.add(v); // adds value to values
                }
            }
        }
        return values;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator(); // iterator for keys
    }
}
