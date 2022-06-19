package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {

    private static Random RANDOM;
    private IFixedSizeQueue<Double> guitar;
    private static final double SAMPLING_RATE = 44100.0;
    private static final double DECAY_FACTOR = 0.996;

    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int cap = (int) (SAMPLING_RATE / frequency) + 1;
        this.guitar = new CircularArrayFixedSizeQueue<>(cap);
        for (int i = 0; i < cap; i++) {
            this.guitar.enqueue(0.0);
        }
    }

    public int length() {
        return this.guitar.size();
    }

    public void pluck() {
        RANDOM = new Random();
        for (int i = 0; i < this.length(); i++) {
            this.guitar.dequeue();
            double rand = 0.5 - RANDOM.nextDouble();
            this.guitar.enqueue(rand);
        }
    }

    public void tic() {
        double front = this.guitar.dequeue();
        double new_front = this.guitar.peek();
        double new_value = (front + new_front) / 2 * DECAY_FACTOR;
        this.guitar.enqueue(new_value);
    }

    public double sample() {
        return this.guitar.peek();
    }
}
