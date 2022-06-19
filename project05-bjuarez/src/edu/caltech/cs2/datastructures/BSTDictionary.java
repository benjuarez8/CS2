package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<? super K>, V>
        implements IDictionary<K, V> {

    protected BSTNode<K, V> root;
    protected int size;

    /**
     * Class representing an individual node in the Binary Search Tree
     */
    protected static class BSTNode<K, V> {
        public final K key;
        public V value;

        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /**
         * Constructor initializes this node's key, value, and children
         */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.left = o.left;
            this.right = o.right;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean hasBothChildren() {
            return this.left != null && this.right != null;
        }
    }

    /**
     * Initializes an empty Binary Search Tree
     */
    public BSTDictionary() {
        this.root = null;
        this.size = 0;
    }


    @Override
    public V get(K key) {
        if (this.root == null) { // checks if root is null
            return null;
        }
        BSTNode<K, V> current_node = this.root; // keeps track of node we are at
        while (!current_node.key.equals(key)) { // loops until key at current node is same as inputted key
            int check = current_node.key.compareTo(key);
            if (check < 0) { // implies that key at current node is "less" than inputted key
                current_node = current_node.right; // travel to right child node
            }
            if (check > 0) { // implies that key at current node is "greater" than inputted key
                current_node = current_node.left; // travel to left child node
            }
            if (current_node == null) { // checks for null nodes
                return null;
            }
        }
        return current_node.value; // if loop is exited, then value at current node is correct value
    }

    @Override
    public V remove(K key) {
        if (!this.containsKey(key) || this.get(key) == null) { // checks if key is in dictionary
            return null;
        }
        this.size--; // decreases size since a node will be removed
        V value_to_return = this.get(key); // holds value
        this.root = this.remove_helper(this.root, key); // recursively calls helper method to traverse children
        return value_to_return;
    }

    private BSTNode<K, V> remove_helper(BSTNode<K, V> current_node, K key) {
        if (current_node == null) { // base case
            return null;
        }
        if (current_node.key.equals(key)) { // checks if current key is same as inputted key
            if (current_node.hasBothChildren()) { // checks if node has left and right children
                BSTNode<K, V> node;
                BSTNode<K, V> new_node = current_node.left; // starts at left node
                while (new_node.right != null) { // traverses down "greater" nodes
                    new_node = new_node.right; // represents max of left subtree (predecessor)
                }
                remove_helper(current_node, new_node.key); // "removes" node
                node = new BSTNode<>(new_node.key, new_node.value); // new node to replace current node
                node.right = current_node.right; // simulates replacing node to be removed
                node.left = current_node.left; // simulates replacing node to be removed
                return node;
            }
            else {
                if (current_node.isLeaf()) { // removed node has no children
                    return null;
                } else if (current_node.left == null) { // removed node has only right child
                    return current_node.right; // right child replaces removed node
                } else if (current_node.right == null) { // removed node has only left child
                    return current_node.left; // left child replaces removed node
                }
            }
        }
        // if key of current node is not the node to be removed
        if (current_node.key.compareTo(key) > 0) { // determines whether to go right or left
            current_node.left = remove_helper(current_node.left, key); // key is "less" than current key
        }
        else {
            current_node.right = remove_helper(current_node.right, key); // key is "greater" than current key
        }
        return current_node;
    }

    @Override
    public V put(K key, V value) {
        if (this.root == null) { // checks if tree is empty
            this.size++; // updates size
            this.root = new BSTNode<>(key, value); // creates new root with inputted data
            return null;
        }
        BSTNode<K, V> current_node = this.root; // starts at root
        V value_to_return = this.get(key); // stores value from inputted key (could be null)
        while (!current_node.key.equals(key)) { // loops until correct node is reached
            int check = current_node.key.compareTo(key);
            if (check < 0) { // current node key < inputted key
                if (current_node.right == null) { // checks if right node is empty
                    current_node.right = new BSTNode<>(key, value); // creates new node
                }
                current_node = current_node.right; // travels to next node
            }
            if (check > 0) { // current node key > inputted key
                if (current_node.left == null) { // checks if left node is empty
                    current_node.left = new BSTNode<>(key, value); // creates new node
                }
                current_node = current_node.left; // travels to next node
            }
        }
        if (value_to_return == null) { // checks if key/value pair already existed
            this.size++; // if not, increases size appropriately
        }
        current_node.value = value; // updates new value
        return value_to_return;
    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of nodes in the BST
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>(); // stores keys
        return this.keys_helper(keys, this.root); // recursively calls helper to travel to all nodes
    }

    private ICollection<K> keys_helper(ICollection<K> keys, BSTNode<K, V> current_node) {
        if (current_node == null) { // base case
            return keys;
        }
        keys.add(current_node.key); // adds current key
        keys = keys_helper(keys, current_node.right); // traverses down right side
        keys = keys_helper(keys, current_node.left); // traverses down left side
        return keys;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> values = new LinkedDeque<>(); // stores values
        return this.values_helper(values, this.root); // recursively calls helper to travel to all nodes
    }

    private ICollection<V> values_helper(ICollection<V> values, BSTNode<K, V> current_node) {
        if (current_node == null) { // base case
            return values;
        }
        values.add(current_node.value); // adds current value
        values = values_helper(values, current_node.right); // traverses down right side
        values = values_helper(values, current_node.left); // traverses down left side
        return values;
    }

    /**
     * Implementation of an iterator over the BST
     */

    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator();
    }

    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.left != null) {
                nodes.enqueue(current.left);
            }
            if (current.right != null) {
                nodes.enqueue(current.right);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }
}
