package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;

public class Graph<V, E> implements IGraph<V, E> {

    private IDictionary<V, IDictionary<V, E>> graph_data;

    public Graph() {
        graph_data = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (this.graph_data.containsKey(vertex)) {
            return false;
        }
        this.graph_data.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
        return true;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if (!this.graph_data.containsKey(src) || !this.graph_data.containsKey(dest)) {
            throw new IllegalArgumentException("Invalid vertices");
        }
        IDictionary<V, E> edge_data = this.graph_data.get(src);
        boolean b = !edge_data.containsKey(dest);
        edge_data.put(dest, e);
        return b;
    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        boolean b1 = this.addEdge(n1, n2, e);
        boolean b2 = this.addEdge(n2, n1, e);
        return b1 && b2;
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if (!this.graph_data.containsKey(src) || !this.graph_data.containsKey(dest)) {
            throw new IllegalArgumentException("Invalid vertices");
        }
        IDictionary<V, E> edge_data = this.graph_data.get(src);
        if (edge_data.containsKey(dest)) {
            edge_data.remove(dest);
            return true;
        }
        return false;
    }

    @Override
    public ISet<V> vertices() {
        return new ChainingHashSet<>(this.graph_data.keys());
//        return this.graph_data.keySet();
    }

    @Override
    public E adjacent(V i, V j) {
        IDictionary<V, E> edge_data = this.graph_data.get(i);
        if (edge_data == null) {
            throw new IllegalArgumentException("Invalid vertices");
        }
        return edge_data.get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        if (!this.graph_data.containsKey(vertex)) {
            throw new IllegalArgumentException("Vertex not in graph");
        }
        return new ChainingHashSet<>(this.graph_data.get(vertex).keys());
//        return this.graph_data.get(vertex).keySet();
    }
}