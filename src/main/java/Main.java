import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Main {

    public static final boolean DEBUG_VERBOSE = false;

    public static void main(String args[]) {

        File [] spamList;
        File [] hamList;
        int spamCount;
        int hamCount;
        DataSet dataSet = new DataSet("Base Dataset");
        DataSet spamDataset = new DataSet("Spam Dataset");
        DataSet hamDataset = new DataSet("Ham Dataset");
        DataSet testDataset = new DataSet("Testing Dataset");
        Scanner sc;


        try {
            File dataSetDir = new File(args[0]);
            if (dataSetDir.isDirectory()) {

                spamList = FileFilter.findSpam(dataSetDir);
                spamCount = spamList.length;
                hamList = FileFilter.findHam(dataSetDir);
                hamCount = hamList.length;



                // for all the spam files in the directory
                for (int i = 0; i < spamCount; i++) {
                    if (DEBUG_VERBOSE) {
                        System.out.println(spamList[i].getName());
                    }

                    // create a new mail
                    Mail mail = new Mail(spamList[i].getName(), true);

                    sc = new Scanner(spamList[i]);

                    while (sc.hasNext()) {
                        String[] tokens = sc.nextLine().split(" ");

                        // refresh the HashMap
                        for (int j = 0; j < tokens.length; j++) {
                            mail.addWord(tokens[j]);
                        }
                    }

                    //update spam data set
                    spamDataset.addMail(mail);
                    dataSet.addMail(mail);
                    sc.close();
                }

                // for all the ham files in the directory
                for (int i = 0; i < hamCount; i++) {
                    if (DEBUG_VERBOSE) {
                        System.out.println(hamList[i].getName());
                    }

                    // create a new mail
                    Mail mail = new Mail(hamList[i].getName(), false);

                    sc = new Scanner(hamList[i]);

                    while (sc.hasNext()) {
                        String[] tokens = sc.nextLine().split(" ");

                        // refresh the HashMap
                        for (int j = 0; j < tokens.length; j++) {
                            mail.addWord(tokens[j]);
                        }
                    }

                    //update spam data set
                    hamDataset.addMail(mail);
                    dataSet.addMail(mail);
                    sc.close();
                }


                if (DEBUG_VERBOSE) {
                    System.out.println(spamDataset.toString());
                    System.out.println(hamDataset.toString());
                    System.out.println(dataSet.toString());

                    double ent = ID3.computeEntropy(dataSet.getMails());

                    System.out.println("The computeEntropy is " + ent);
                }

                File testingDir = new File(args[1]);
                if (testingDir.isDirectory()) {
                    spamList = FileFilter.findSpam(testingDir);
                    spamCount = spamList.length;
                    hamList = FileFilter.findHam(testingDir);
                    hamCount = hamList.length;

                    // for all the spam files in the directory
                    for (int i = 0; i < spamCount; i++) {
                        if (DEBUG_VERBOSE) {
                            System.out.println(spamList[i].getName());
                        }

                        // create a new mail
                        Mail mail = new Mail(spamList[i].getName(), true);

                        sc = new Scanner(spamList[i]);

                        while (sc.hasNext()) {
                            String[] tokens = sc.nextLine().split(" ");

                            // refresh the HashMap
                            for (int j = 0; j < tokens.length; j++) {
                                mail.addWord(tokens[j]);
                            }
                        }

                        //update test data set
                        testDataset.addMail(mail);
                        sc.close();
                    }

                    // for all the ham files in the directory
                    for (int i = 0; i < hamCount; i++) {
                        if (DEBUG_VERBOSE) {
                            System.out.println(hamList[i].getName());
                        }

                        // create a new mail
                        Mail mail = new Mail(hamList[i].getName(), false);

                        sc = new Scanner(hamList[i]);

                        while (sc.hasNext()) {
                            String[] tokens = sc.nextLine().split(" ");

                            // refresh the HashMap
                            for (int j = 0; j < tokens.length; j++) {
                                mail.addWord(tokens[j]);
                            }
                        }

                        //update test data set
                        testDataset.addMail(mail);
                        sc.close();
                    }
                }

                int sum = testDataset.getMails().size();
                int successful = 0;

                String c;
                for (Mail mail :
                        testDataset.getMails()) {

                    TreeNode<String> category = ID3.determineIfSpam(dataSet, mail.getWordsAsList(), false);

                    if ((category.findTreeNode("YES") != null && mail.isSpam()) ||
                            (category.findTreeNode("NO") != null && !mail.isSpam())) { // brute-force JUST TO FIND SOMETHING!!
                        successful++;
                        c = "NO";
                        if (category.findTreeNode("YES").data == "YES") c = "YES";
                        System.out.println("Mail " + mail.getId() + " is " + c);
                    } else System.out.println("Failed to categorize " + mail.getId());
                }

                int s = (successful / sum) * 100;
                System.out.println(s);

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please give a Dataset Directory as an argument when calling the program.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
