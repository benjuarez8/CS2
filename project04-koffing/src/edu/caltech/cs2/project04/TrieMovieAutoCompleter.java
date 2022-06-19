package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashSet;
import java.util.Set;

public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() { // maps suffixes to movie titles
        titles.clear();
        for (String title : ID_MAP.keySet()) { // loops through titles
            String[] title_split = title.split(" "); // creates array of words in title separated by space
            IDeque<String> suffixes = new LinkedDeque<>(); // holds suffixes
            for (String s : title_split) { // loops through title words
                suffixes.addBack(s.toLowerCase()); // adds each word (lowercase) to suffixes
            }
            for (int i = 0 ; i < title_split.length; i++) {
                IDeque<String> value = new LinkedDeque<>(); // holds title name
                if (titles.containsKey(suffixes)) { // checks if same suffix key is already mapped
                    value = titles.get(suffixes); // gets the title that is already mapped to suffixes
                }
                value.add(title); // adds new title to value from suffix key
                titles.put(suffixes, value); // adds appropriate key, value to titles
                suffixes.removeFront(); // creates new suffix from title words
            }
        }
    }

    public static IDeque<String> complete(String term) {
        Set<String> results = new HashSet<>(); // holds possible movie names
        IDeque<String> term_words = new LinkedDeque<>(); // holds words from input
        String[] term_split = term.split(" ");
        for (String s : term_split) {
            term_words.addBack(s.toLowerCase()); // adds distinct words for input to IDeque
        }
        IDeque<IDeque<String>> possibilities = titles.getCompletions(term_words); // finds possible movies
        for (IDeque<String> movie : possibilities) {
            for (String m : movie) {
                results.add(m);
            }
        }
        IDeque<String> r = new LinkedDeque<>();
        for (String s : results) {
            r.add(s);
        }
        return r;
    }
}
