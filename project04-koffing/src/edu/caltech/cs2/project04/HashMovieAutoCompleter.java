package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {

    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() { // maps movie titles to suffixes
        titles.clear();
        // loop through each key in ID_MAP
        // break apart each movie title into "suffixes"
        // store movie title as key and IDeque of suffixes as value
        for (String title : getIDMap().keySet()) {
            IDeque<String> suffixes = new ArrayDeque<>();
            suffixes.addBack(title);
            int idx = title.indexOf(' ');
            String t = title;
            while (idx >= 0) {
                suffixes.addBack(t.substring(idx + 1));
                t = t.substring(idx + 1);
                idx = t.indexOf(' ');
            }
            titles.put(title, suffixes);
        }
    }

    public static IDeque<String> complete(String term) {
        // check if term is a prefix of any of the suffixes for each movie title
        // if so, add movie title to ArrayDeque to return
        IDeque<String> matches = new ArrayDeque<>();
        for (String title : titles.keySet()) {
            for (String suffix : titles.get(title)) {
                if (suffix.length() >= term.length()) {
                    String prefix = suffix.substring(0, term.length()).toLowerCase();
                    if (prefix.equals(term.toLowerCase())) {
                        if (suffix.length() >= term.length() + 1 && suffix.charAt(term.length()) == ' ') {
                            if (!matches.contains(title)) {
                                matches.addBack(title);
                            }
                        }
                        else if (suffix.length() == term.length()) {
                            if (!matches.contains(title)) {
                                matches.addBack(title);
                            }
                        }
                    }
                }
            }
        }
        return matches;
    }
}
