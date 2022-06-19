package edu.caltech.cs2.textgenerator;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;

public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;

    public static String normalize(String s) {
        return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (String word : x) {
            this.data.add(word);
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        Iterator<String> dataIterator = this.data.iterator();
        dataIterator.next();
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = dataIterator.next();
        }
        data[data.length - 1] = word;
        return new NGram(data);
    }

    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ? "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }

    @Override
    public int compareTo(NGram other) {
        Iterator<String> it1 = this.data.iterator();
        Iterator<String> it2 = other.data.iterator();
        for (int i = 0; i < this.data.size(); i++) {
            if (!it2.hasNext()) {
                return 1;
            }
            int check = it1.next().compareTo(it2.next());
            if (check != 0) {
                return check;
            }
        }
        if (it2.hasNext()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NGram)) {
            return false;
        }
        NGram n = (NGram) o;
        if (this.data.size() != n.data.size()) {
            return false;
        }
        Iterator<String> it1 = this.data.iterator();
        Iterator<String> it2 = n.data.iterator();
        while (it1.hasNext()) {
            if (!it1.next().equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (String word : this.data) {
            result = 31 * result + word.hashCode();
        }
        if (result < 0) {
            return result * -1;
        }
        return result;
    }
}