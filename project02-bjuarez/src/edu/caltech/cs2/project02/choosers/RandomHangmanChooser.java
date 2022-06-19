package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomHangmanChooser implements IHangmanChooser {

  private static final Random RANDOM = new Random();
  private final String word;
  private int num_guesses;
  private SortedSet<Character> guessed_letters = new TreeSet<>();

  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
      // Checks for valid inputs
      if (wordLength < 1 || maxGuesses < 1) {
          throw new IllegalArgumentException("ERROR: Invalid inputs");
      }
      // Sets up appropriate SortedSets and reads in scrabble.txt
      SortedSet<String> words = new TreeSet<>(); // Holds words from scrabble.txt
      SortedSet<String> valid_words = new TreeSet<>(); // Holds words of appropriate length
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
      int idx = RANDOM.nextInt(valid_words.size()); // Random number of times valid_words will be iterated over
      int count = 0; // Tracks # of iterations
      String word_temp = "";
      // Iterates random number of times and selects word
      for (String z : valid_words) {
          if (count == idx) {
              word_temp = z;
          }
          count++;
      }
      this.word = word_temp; // Assigns selected word to this.word
      this.num_guesses = maxGuesses; // Established starting number of guesses available
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
      int occurences = 0; // tracks occurrences of guessed letter in secret word
      for (char c : this.word.toCharArray()) {
          if (c == letter) {
              occurences++;
          }
      }
      this.guessed_letters.add(letter); // Keeps track of guessed letters
      if (occurences == 0) {
          this.num_guesses--; // Updates number of guesses remaining
      }
      return occurences;
  }

  @Override
  public boolean isGameOver() {
      return getPattern().equals(word) || this.num_guesses == 0;
  }

  @Override
  public String getPattern() {
      String pattern = "";
      // Loops through characters of secret word
      for (char c : this.word.toCharArray()) {
          // Checks if secret word contains any of the guessed letters
          if (this.guessed_letters.contains(c)) {
              pattern += Character.toString(c); // Places correctly guessed letters appropriately
          }
          else {
              pattern += "-"; // Places dashes for letters not yet guessed
          }
      }
      return pattern;
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
      return this.word;
  }
}