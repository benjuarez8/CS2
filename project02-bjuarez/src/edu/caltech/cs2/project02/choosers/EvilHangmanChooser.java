package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {

  private static final Random RANDOM = new Random();
  private List<String> best_family = new ArrayList<>();
  private String best_pattern;
  private SortedSet<Character> guessed_letters = new TreeSet<>();
  private int num_guesses;

  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    // Checks for valid inputs
    if (wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException("ERROR: Invalid inputs");
    }

    // Sets up appropriate SortedSets and reads in scrabble.txt
    SortedSet<String> words = new TreeSet<>(); // Holds words from scrabble.txt
    List<String> valid_words = new ArrayList<>(); // Holds words of appropriate length
//    File file = new File("/Users/benjuarez/IdeaProjects/project02-bjuarez/data/scrabble.txt");
    File file = new File("data/scrabble.txt");

    Scanner scanner = new Scanner(file);

    // Places words from scrabble.txt into SortedSet
    String w;
    boolean validLength = false;
    while (scanner.hasNextLine()) {
      w = scanner.nextLine();
      if (w.length() == wordLength) {
        validLength = true;
      }
      words.add(w);
    }

    // Check that words of length wordLength exist
    if (!validLength) {
      throw new IllegalStateException("ERROR: Invalid wordLength");
    }
    // Filter out words of invalid length
    for (String y : words) {
      if (y.length() == wordLength) {
        valid_words.add(y);
      }
    }
    this.best_pattern = ""; // initial blank pattern
    for (int i = 0; i < wordLength; i++) {
      this.best_pattern += "-";
    }
    this.best_family = valid_words;
    this.num_guesses = maxGuesses; // Established starting number
  }

  @Override
  public int makeGuess(char letter) {
    // Checks number of guesses remaining
    if (getGuessesRemaining() == 0) { //CHECK
      throw new IllegalStateException("ERROR: No more guesses left");
    }
    // Checks for previously guessed characters
    if (getGuesses().contains(letter)) {
      throw new IllegalArgumentException("ERROR: This letter has already been guessed");
    }
    // Checks that guessed letter is lowercase
    if (!Character.isLowerCase(letter)) {
      throw new IllegalArgumentException("ERROR: Letter must be lowercase");
    }
    this.guessed_letters.add(letter); // Updates guessed letters
    // Creates new map initially with best pattern and best family
    Map<String, List<String>> families = new TreeMap<>();
    families.put(this.best_pattern, this.best_family);

    // Removes words from best_family if word contains new guessed letter
    List<String> contains_letter = new ArrayList<>();
    for (String w : this.best_family) {
      if (w.indexOf(letter) >= 0) {
        contains_letter.add(w);
      }
    }
    for (String w : contains_letter) {
      this.best_family.remove(w);
      families.replace(this.best_pattern, this.best_family);
    }
    // Places words in appropriate family or sets aside for later addition with new pattern
    for (String word : contains_letter) {
      List<String> temp;
      boolean add = false;
      boolean added = false;
      for (String key : families.keySet()) {
        String pattern_check = "";
        for (char c : word.toCharArray()) {
          if (guessed_letters.contains(c)) {
            pattern_check += c;
          }
          else {
            pattern_check += "-";
          }
        }
        if (key.equals(pattern_check)) {
          temp = families.get(key);
          temp.add(word);
          families.replace(key, temp);
          added = true;
        }
      }
      // create new key for unmatched word using containsKey()
      if (!added) { // Creates new key for unmatched words
        String new_pattern = "";
        List<String> new_temp = new ArrayList<>();
        new_temp.add(word);
        for (char c : word.toCharArray()) {
          if (guessed_letters.contains(c)) { // Creates new pattern by checking each character with guessed letter
            new_pattern += Character.toString(c);
          }
          else {
            new_pattern += "-";
          }
        }
        families.put(new_pattern, new_temp); // Adds new pattern to map, adds unmatched word to pattern family
      }
    }
    // Chooses new best_family and best_pattern
    int max_family_size = 0;
    String previous_best_pattern = this.best_pattern;
    for (String key : families.keySet()) { // Loops through keys
      int family_size = families.get(key).size();
      if (family_size > max_family_size) { // Compares sizes of keys
        this.best_pattern = key;
        this.best_family = families.get(this.best_pattern);
        max_family_size = family_size;
      }
      else if (families.get(key).size() == max_family_size) { // Special case
        if (this.best_pattern.compareTo(key) > 0) { // Choose pattern that is alphabetically first
          this.best_pattern = key;
          this.best_family = families.get(this.best_pattern);
          max_family_size = family_size;
        }
      }
    }
    // Tracks occurrences of guessed letter
    int occurences = 0;
    for (int z = 0; z < this.best_pattern.length(); z++) {
      if (previous_best_pattern.charAt(z) == '-' && this.best_pattern.charAt(z) != '-') {
        occurences++;
      }
    }
    if (occurences == 0) {
      this.num_guesses--; // Updates number of guesses remaining
    }
    return occurences;
  }

  @Override
  public boolean isGameOver() {
//    return getPattern().equals(this.best_pattern) || this.num_guesses == 0;
    return !this.best_pattern.contains("-") || this.num_guesses == 0;
  }

  @Override
  public String getPattern() {
    return this.best_pattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.guessed_letters;
  }

  @Override
  public int getGuessesRemaining() {
    return this.num_guesses;
  }

  @Override
  public String getWord() {
    this.num_guesses = 0;
    return this.best_family.get(0);
  }
}