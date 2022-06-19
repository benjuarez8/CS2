package edu.caltech.cs2.lab08;

import java.util.*;
import java.util.stream.Collectors;

public class PebblingGraph {
    public final Set<PebblingNode> vertices;

    public static class PebblingNode {
        public final int id;
        public final Set<PebblingNode> neighbors;
        public int inDegree;

        public PebblingNode(int id) {
            this.id = id;
            this.neighbors = new HashSet<>();
            this.inDegree = 0;
        }

        public String toString() {
            return this.id + " -> {" + this.neighbors.stream().map(v -> "" + v.id).sorted().collect(Collectors.joining(", ")) + "}";
        }
    }

    public PebblingGraph() {
        this.vertices = new HashSet<>();
    }

    public PebblingNode addVertex(int id) {
        PebblingNode v = new PebblingNode(id);
        this.vertices.add(v);
        return v;
    }

    public void addEdge(PebblingNode fromVertex, PebblingNode toVertex) {
        if (!this.vertices.contains(fromVertex) || !this.vertices.contains(toVertex)) {
            throw new IllegalArgumentException("Vertices don't exist in graph");
        }

        fromVertex.neighbors.add(toVertex);
        toVertex.inDegree++;
    }

    public List<Integer> toposort() {
        List<Integer> ids = new ArrayList<>();
        Hashtable<PebblingNode, Integer> degrees = new Hashtable<>();
        List<PebblingNode> worklist = new ArrayList<>();

        for (PebblingNode node : this.vertices) {
            degrees.put(node, node.inDegree);
            if (degrees.get(node) == 0) {
                worklist.add(node);
            }
        }

        while (!worklist.isEmpty()) {
            PebblingNode curr = worklist.get(0);
            ids.add(curr.id);
            for (PebblingNode new_node : curr.neighbors) {
                degrees.put(new_node, degrees.get(new_node) - 1);
                if (degrees.get(new_node) == 0) {
                    worklist.add(new_node);
                }
            }
            worklist.remove(curr);
        }

        return ids;
    }

}