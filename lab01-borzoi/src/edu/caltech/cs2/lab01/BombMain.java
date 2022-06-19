package edu.caltech.cs2.lab01;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BombMain {
    public static void main(String[] args) {
        Bomb b = new Bomb();
        b.phase0("22961293");
        b.phase1("hdc");

        String[] password = new String[5001];
        for (int i = 0; i < password.length; i++) {
            password[i] = "0";
        }

        password[5000] = "1374866960";
        String p = String.join(" ", password);
        b.phase2(p);
    }
}