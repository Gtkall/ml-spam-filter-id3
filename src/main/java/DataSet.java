import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataSet {

    private String id;

    private ArrayList<Mail> mails;
    private Map<String, Integer> lexicon;

    public DataSet(ArrayList<Mail> mails) {
        this.id = Long.toHexString(Double.doubleToLongBits(Math.random()));
        this.mails = new ArrayList<>();
        this.lexicon = new HashMap<>();
        setMails(mails);
    }

    public DataSet(DataSet data) {
        this.id = data.id;
        this.mails = new ArrayList<>();
        this.mails.addAll(data.mails);
        this.lexicon = new HashMap<>(data.lexicon);
    }


    public DataSet(String id) {
        this.id = id;
        mails = new ArrayList<>();
        lexicon = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Mail> getMails() {
        return this.mails;
    }

    public ArrayList<Mail> getMails(String word, boolean exists) {
        ArrayList<Mail> output = new ArrayList<>();

        if (exists) {
            for (Mail mail :
                    this.mails) {
                if (mail.getWord(word) > 0) {
                    output.add(mail);
                }
            }
        } else {
            for (Mail mail :
                    this.mails) {
                if (mail.getWord(word) == 0) {
                    output.add(mail);
                }
            }
        }

        return output;
    }

    public ArrayList<Mail> getMails(boolean isSpam) {
        ArrayList<Mail> mails = new ArrayList<>();
        for (Mail mail: this.mails) {
            if (mail.isSpam() == isSpam) {
                mails.add(mail);
            }
        }

        return mails;
    }

    public void setMails(ArrayList<Mail> mails) {
        this.mails = mails;
        this.updateLexicon(mails);
    }

    public void addMail(Mail mail) {
        this.mails.add(mail);
        this.updateLexicon(mail);
    }

    public void setLexicon(Map<String, Integer> lexicon) {
        this.lexicon = lexicon;
    }

    public void updateLexicon(Mail mail) {
        Map<String, Integer> words = mail.getWords();
        Set<String> keywords = words.keySet();
        int count;
        for (String keyword : keywords) {
            count = 0;
            if (!lexicon.containsKey(keyword)) { // word doesn't exist
                lexicon.put(keyword, words.get(keyword));
            } else { //keyword exists
                count = lexicon.get(keyword) + words.get(keyword);
                lexicon.put(keyword, count);
            }
        }

    }

    public void updateLexicon(ArrayList<Mail> mails) {
        for (Mail mail : mails) {
            Map<String, Integer> words = mail.getWords();
            Set<String> keywords = words.keySet();
            int count;
            for (String keyword : keywords) {
                count = 0;
                if (!lexicon.containsKey(keyword)) { // word doesn't exist
                    lexicon.put(keyword, words.get(keyword));
                } else { //keyword exists
                    count = lexicon.get(keyword) + words.get(keyword);
                    lexicon.put(keyword, count);
                }
            }
        }
    }

    public String toString() {
        return ('\n'
                + id
                + '\n'
                + mails.toString()
                + '\n'
                + '\n'
                + lexicon.toString()
                + '\n');

    }
}
