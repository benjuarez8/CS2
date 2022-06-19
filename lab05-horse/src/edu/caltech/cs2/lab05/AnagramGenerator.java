package edu.caltech.cs2.lab05;

import java.util.ArrayList;
import java.util.List;

public class AnagramGenerator {
    public static void printPhrases(String phrase, List<String> dictionary) {
        List<String> acc = new ArrayList<>();
        LetterBag bag = new LetterBag(phrase);
        printPhrases(acc, dictionary, bag);
    }

    private static void printPhrases(List<String> acc, List<String> dictionary, LetterBag bag) {
        if (bag.isEmpty()) {
            System.out.println(acc.toString());
            return;
        }
        for (String word : dictionary) { // loop through words in dictionary
            LetterBag dict_word = new LetterBag(word); // create letter bag for dictionary word
            LetterBag new_bag = bag.subtract(dict_word); // letters that are left to use
            if (new_bag != null) { // checks if dict_word can be made from phrase
                acc.add(word); // adds word to accumulator
                printPhrases(acc, dictionary, new_bag); // recursively call with updated accumulator and new bag
                acc.remove(word);

            }
        }
    }

    public static void printWords(String word, List<String> dictionary) {
        LetterBag bag = new LetterBag(word);
        for (String w : dictionary) {
            if (w.length() == word.length()) {
                LetterBag dict_word = new LetterBag(w);
                LetterBag new_bag = bag.subtract(dict_word);
                if (new_bag != null && new_bag.isEmpty()) {
                    System.out.println(w);
                }
            }
        }
    }
}
