package edu.caltech.cs2.project01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;

public class Decryption {
    public static void main(String[] args) throws IOException {
        File f = new File("cryptogram.txt");
        Scanner s = new Scanner(f);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String line = "";
        String ciphertext = "";
        while (s.hasNextLine()) {
            line = s.nextLine();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (alphabet.indexOf(c) != -1) {
                    ciphertext += Character.toString(c);
                }
            }
        }
        SubstitutionCipher sc = new SubstitutionCipher(ciphertext);
        QuadGramLikelihoods likelihoods = new QuadGramLikelihoods();
        sc = sc.getSolution(likelihoods);
        String decrypted_text = sc.getPlainText();
        FileWriter bob = new FileWriter("plaintext.txt");
        bob.write(decrypted_text);
        bob.close();
    }
}
