package edu.caltech.cs2.project01;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();

    /**
     * Construct a SubstitutionCipher with the given cipher text and key
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;
    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        this.ciphertext = ciphertext;
        this.key = new HashMap<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            char l = alphabet.charAt(i);
            this.key.put(l, l);
        }
        for (int i = 0; i < 10000; i++) {
            this.key = this.randomSwap().key;
        }
    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        return ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {
        String plainText = "";
        for (int i = 0; i < this.ciphertext.length(); i++) {
            char c = this.ciphertext.charAt(i);
            if (this.key.containsKey(c)) {
                plainText += Character.toString(this.key.get(c));
            }
        }
        return plainText;
    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<Character, Character> new_key = new HashMap<>(this.key);
        int idx1 = RANDOM.nextInt(alphabet.length());
        int idx2 = RANDOM.nextInt(alphabet.length());
        while (idx1 == idx2) {
            idx2 = RANDOM.nextInt(alphabet.length());
        }
        char key1 = alphabet.charAt(idx1);
        char key2 = alphabet.charAt(idx2);
        char value1 = new_key.get(key1);
        char value2 = new_key.get(key2);
        new_key.put(key1, value2);
        new_key.put(key2, value1);
        SubstitutionCipher sc = new SubstitutionCipher(this.ciphertext, new_key);
        return sc;
    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods) {
        String plainText = getPlainText();
        double sum = 0;
        for (int i = 0; i < plainText.length() - 3; i++) {
            String q = "";
            q = plainText.substring(i, i + 4);
            sum += likelihoods.get(q);
        }
        return sum;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        SubstitutionCipher sc1 = new SubstitutionCipher(this.ciphertext);
        int trials = 0;
        while (trials < 1000) {
            SubstitutionCipher sc2 = sc1.randomSwap();
            if (sc2.getScore(likelihoods) > sc1.getScore(likelihoods)) {
                sc1 = sc2;
                trials = 0;
            }
            trials++;
        }
        return sc1;
    }
}
