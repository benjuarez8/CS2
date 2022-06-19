package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AIHangmanGuesser implements IHangmanGuesser {

//  private static final String dictionary = "/Users/benjuarez/IdeaProjects/project02-bjuarez/data/scrabble.txt";
  private static final String dictionary = "data/scrabble.txt";


  @Override
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {
    SortedSet<String> words = new TreeSet<>();
    File file = new File(dictionary);
    Scanner scanner = new Scanner(file);
    String w;
    String pattern_check;
    // Finds all words in dictionary that matches given pattern
    while (scanner.hasNextLine()) {
      pattern_check = "";
      w = scanner.nextLine();
      if (w.length() == pattern.length()) {
        for (char c : w.toCharArray()) {
          if (guesses.contains(c)) {
            pattern_check += Character.toString(c);
          }
          else {
            pattern_check += "-";
          }
        }
        if (pattern.equals(pattern_check)) {
          words.add(w);
        }
      }
    }
    // Loops through unguessed letters to find the number of occurrences in the remaining words
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    Map<Character, Integer> unguessed_letters = new TreeMap<>();
    for (char l : alphabet.toCharArray()) { // Finds unguessed letters and initializes frequencies to 0
      if (!guesses.contains(l)) {
        unguessed_letters.put(l, 0);
      }
    }
    for (String p : words) {
      for (char letter : p.toCharArray()) {
        if (unguessed_letters.containsKey(letter)) {
          int f = unguessed_letters.get(letter) + 1;
          unguessed_letters.replace(letter, f);
        }
        else if (!guesses.contains(letter)){
          unguessed_letters.put(letter, 0);
        }
      }
    }
    // Finds letter with highest frequency
    char letter_to_return = 123;
    int highest_freq = -1;
    for (char key : unguessed_letters.keySet()) {
      if (unguessed_letters.get(key) > highest_freq) {
        highest_freq = unguessed_letters.get(key);
        letter_to_return = key;
      }
      else if (unguessed_letters.get(key) == highest_freq) {
        if (key < letter_to_return) {
          letter_to_return = key;
        }
      }
    }
    return letter_to_return;
  }
}
