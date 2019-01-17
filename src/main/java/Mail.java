import java.util.*;

public class Mail {

    private String id;
    private Map<String, Integer> words;
    private final boolean isSpam;


    /**
     * public constructor
     */
    public Mail(String id, boolean isSpam) {
        this.id = id;
        words = new HashMap<>();
        this.isSpam = isSpam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * returns a set of the words in this mail
     * @return a set of the words
     */
    public Map<String, Integer> getWords() {
        return words;
    }

    public List<String> getWordsAsList() {
        List<String> words = new ArrayList<String>(this.words.keySet());
        return words;
    }

    /**
     * sets the words of a mail
     * @param words the words hash map
     */
    public void setWords(Map<String, Integer> words) {
        this.words = words;
    }

    /**
     * returns the number of times a word occurs in the mail
     * @param word the word to search for
     * @return word occurrence
     */
    public int getWord(String word) {
        int c = (this.words.get(word) == null) ? 0 : this.words.get(word);
        return c;
    }

    /**
     * increases the occurrence of a word
     * @param word the word to step up
     */
    public void addWord(String word) {
        int count = (this.words.get(word) != null) ? this.words.get(word) : 0;
        this.words.put(word, ++count);
    }

    /**
     * checks if a mail is spam
     * @return true if spam
     */
    public boolean isSpam() {
        return isSpam;
    }

    public String toString() {
        if(isSpam) {
            return ('\n'
                    + id + "(SPAM) "
                    + '\n'
                    + words.toString()
                    + '\n');
        } else {
            return ('\n'
                    + id + "(HAM) "
                    + '\n'
                    + words.toString()
                    + '\n');
        }
    }

}
