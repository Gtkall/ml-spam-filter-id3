import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.log;

public class ID3 {


    private static final boolean DEBUG_VERBOSE = true;

    public static double computeEntropy(ArrayList<Mail> mails) {

        double entropy;
        double spam = 0;
        double ham = 0;
        double p_spam;
        double p_ham;
        double sum = mails.size();

        for (Mail mail :
                mails) {
            if (mail.isSpam()) {
                spam++;
            } else {
                ham++;
            }
        }

        p_spam = spam / sum;
        p_ham = ham / sum;

        entropy = -p_spam * log(2, p_spam) - p_ham * log(2, p_ham);

        return entropy;

    }

    public static double computeEntropyOfAttribute (ArrayList<Mail> mails, String word, boolean exists) {

        double entropy;
        double spam = 0;
        double ham = 0;
        double p_spam;
        double p_ham;
        int sum = mails.size();

        if (exists) {
            for (Mail mail :
                    mails) {
                if (mail.getWord(word) != 0) {
                    if (mail.isSpam()) {
                        spam++;
                    } else {
                        ham++;
                    }
                }
            }
        } else {
            for (Mail mail :
                    mails) {
                if (mail.getWord(word) == 0) {
                    if (mail.isSpam()) {
                        spam++;
                    } else {
                        ham++;
                    }
                }
            }
        }

        p_spam = spam / sum;
        p_ham = ham / sum;

        entropy = -p_spam * log(2, p_spam) - p_ham * log(2, p_ham);

        if (DEBUG_VERBOSE) {
            System.out.println("Entropy of " + word + " is " + entropy);
        }

        return entropy;
    }

    public static double computeInfoGain (DataSet dataSet, String word) {

        double ig;
        double sum_wordExists = 0;
        double p_wordExists;
        int sum = dataSet.getMails().size();

        for (Mail mail :
                dataSet.getMails()) {
            if (mail.getWord(word) >= 0) {
                sum_wordExists++;
            }
        }
        p_wordExists = sum_wordExists / sum;

        double dsEntropy = computeEntropy(dataSet.getMails());

        double wordExistsEntropy = computeEntropyOfAttribute(dataSet.getMails(), word, true);
        double wordNotExistsEntropy = computeEntropyOfAttribute(dataSet.getMails(), word, false);

        ig = dsEntropy - (p_wordExists * wordExistsEntropy) - ((1-p_wordExists) * wordNotExistsEntropy);

        if (DEBUG_VERBOSE) {
            System.out.println("Info Gain of word " + word + " is " + ig);
        }

        return ig;
        
    }

    public static String chooseBestAttribute (DataSet dataSet, List<String> attributes) {
        double max = 0;
        int pointer = 0;
        double curInfoGain = 0;

        for (int i = 0; i < attributes.size(); i++) {
            String attribute = attributes.get(i);
            curInfoGain = computeInfoGain(dataSet, attribute);
            if (curInfoGain > max) {
                max = curInfoGain;
                pointer = i;
            }
        }

        if (DEBUG_VERBOSE) {
            System.out.println("Best attribute chosen: " + attributes.get(pointer));
        }

        return attributes.get(pointer);
    }

    public static TreeNode<String> determineIfSpam(DataSet dataSet, List<String> attributes, boolean isSpam) {

        TreeNode<String> tn;
        if (dataSet.getMails().isEmpty()) { // no more mails in the current dataset
            if (isSpam) return new TreeNode<>("YES");
            else return new TreeNode<>("NO");
        } else if (checkIfSpam(dataSet)) { // if 100% of mails are spam
            return new TreeNode<>("YES");
        } else if (checkIfHam(dataSet)) { // if 100% of mails are ham
            return new TreeNode<>("NO");
        } else if (attributes.isEmpty()) { // if there are no more attributes to evaluate
            if (checkDataSet(dataSet)) return new TreeNode<>("YES");
            else return new TreeNode<>("NO");
        } else {
            String attribute = chooseBestAttribute(dataSet, attributes);
            tn = new TreeNode<>(attribute);
            boolean m = checkDataSet(dataSet);

            attributes.remove(attribute);

            DataSet subSet1 = new DataSet(dataSet.getMails(attribute, true));
            TreeNode<String> child1 = determineIfSpam(subSet1, attributes, m);
            tn.addChild(child1.data);

            DataSet subSet2 = new DataSet(dataSet.getMails(attribute, false));
            TreeNode<String> child2 = determineIfSpam(subSet2, attributes, m);
            tn.addChild(child2.data);

            if (DEBUG_VERBOSE) {
                System.out.println("Current level: " + tn.getLevel());
            }
            return tn;
        }
    }

    private static boolean checkDataSet(DataSet dataSet) {
        int spamSum = 0;
        int hamSum = 0;

        for (Mail mail :
                dataSet.getMails()) {
            if (mail.isSpam()) spamSum++;
            else hamSum++;
        }

        return spamSum >= hamSum;
    }

    private static boolean checkIfSpam(DataSet dataSet) { // TODO: maybe implement check on DataSet class
        int sum = 0;

        for (Mail mail:
             dataSet.getMails()) {
            if (mail.isSpam()) sum++;
        }

        return sum == dataSet.getMails().size();
    }

    private static boolean checkIfHam(DataSet dataSet) { // TODO: maybe implement check on DataSet class
        int sum = 0;

        for (Mail mail:
                dataSet.getMails()) {
            if (!mail.isSpam()) sum++;
        }

        return sum == dataSet.getMails().size();
    }

}


