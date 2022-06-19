package edu.caltech.cs2.lab07;

import java.util.*;

public class GrundyPosition {
    /*
     * Stores a mapping from the height of a pile to how many of those piles exist.
     * Does not include piles of size less than three.
     */
    private SortedMap<Integer, Integer> heapCounts;
    private static HashMap<GrundyPosition, Boolean> mem_table = new HashMap<GrundyPosition, Boolean>();

    /**
     * Initializes a GrundyPosition with a single heap of height heapHeight.
     **/
    public GrundyPosition(int heapHeight) {
        this.heapCounts = new TreeMap<Integer, Integer>();
        this.heapCounts.put(heapHeight, 1);
    }

    public GrundyPosition(SortedMap<Integer, Integer> sm) { // used for getMoves()
        this.heapCounts = sm;
    }

    /**
     * Returns a list of legal GrundyPositions that a single move of Grundy's Game
     * can get to.
     **/
    public List<GrundyPosition> getMoves() {
        List<GrundyPosition> legal_moves = new ArrayList<>(); // stores list of legal GrundyPositions
        for (int heap : this.heapCounts.keySet()) { // loops through heap sizes
            for (int i = 1; i < heap / 2.0; i++) {  // used to keep track of new heap sizes
                SortedMap<Integer, Integer> copy = new TreeMap<>(this.heapCounts); // used to get info from this GP
                GrundyPosition gp = new GrundyPosition(copy); // new GP to put in legal_moves
                int new_heap = heap - i; // new potential heap size (along with i)
                if (i > 2) { // checks valid heap size
                    if (gp.heapCounts.containsKey(i)) { // checks if heap size already exists
                        gp.heapCounts.put(i, this.heapCounts.get(i) + 1); // increases count
                    }
                    else {
                        gp.heapCounts.put(i, 1); // puts new heap size in with count of 1
                    }
                }
                if (new_heap > 2) { // checks valid heap size
                    if (gp.heapCounts.containsKey(new_heap)) { // checks if heap size already exists
                        gp.heapCounts.put(new_heap, this.heapCounts.get(new_heap) + 1); // increases count
                    }
                    else {
                        gp.heapCounts.put(new_heap, 1); // puts new heap size in with count of 1
                    }
                }
                gp.heapCounts.put(heap, this.heapCounts.get(heap) - 1); // decrements starting heap size
                if (gp.heapCounts.get(heap) == 0) { // checks if starting heap size should be present
                    gp.heapCounts.remove(heap);
                }
                legal_moves.add(gp); // adds new GP to legal_moves
            }
        }
        return legal_moves;
    }

    public boolean isTerminalPosition() {
        for (int heap : this.heapCounts.keySet()) {
            if (heap > 2) {
                return false;
            }
        }
        return true;
    }

    public boolean isPPosition() {
        if (this.isTerminalPosition()) {
            mem_table.put(this, true);
            return true;
        }
        if (mem_table.containsKey(this)) {
            return mem_table.get(this);
        }
        for (GrundyPosition gp : this.getMoves()) {
            if (!gp.isNPosition()) {
                mem_table.put(this, false);
                return false;
            }
        }
        mem_table.put(this, true);
        return true;
    }

    public boolean isNPosition()  {
        if (mem_table.containsKey(this)) {
            return !mem_table.get(this);
        }
        for (GrundyPosition gp : this.getMoves()) {
            if (gp.isPPosition()) {
                mem_table.put(this, false);
                return true;
            }
        }
        mem_table.put(this, true);
        return false;
    }
    
    /** 
     * Ignore everything below this point.
     **/

    @Override
    public int hashCode() {
       return this.heapCounts.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GrundyPosition)) {
            return false;
        }
        return this.heapCounts.equals(((GrundyPosition) o).heapCounts);
    }

    @Override
    public String toString() {
        return this.heapCounts.toString();
    }
}