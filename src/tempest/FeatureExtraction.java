package tempest;

import tempest.XmlParser.XmlParserByXpath;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class FeatureExtraction {

    /**
     * split the sentence into subsets of words
     * @param List<String> sentenceList
     * @return List<String[]> splittedSentenceList
     */
    public static List<String[]> splitSentence(List<String> sentenceList) {
        List<String[]> splittedSentenceList = new ArrayList<String[]>();
        for(String sentence : sentenceList) {
            String[] splittedSentence = sentence.split("[\\s]+");
            /*
            for (int i=0; i<splittedSentence.length; i++){
                System.out.println( splittedSentence[i] );
            }
            */
            splittedSentenceList.add( splittedSentence );
        }
        return splittedSentenceList;
    }

    /**
     * get all word set
     * @param List<String[]> splittedSentenceList
     * @return HashSet<String> allWordSet
     */
    public static HashSet<String> getAllWordSet(List<String[]> splittedSentenceList) {
        HashSet<String> allWordSet = new HashSet<String>();
        for(String[] splittedSentence : splittedSentenceList) {
            for(String word : splittedSentence) {
                allWordSet.add(word);
            }
        }

        return allWordSet;
    }

    /**
     * get the feature vector
     * @param List<String[]> splittedSentenceList
     * @param HashSet<String> allWordSet
     * @return List<int[]> featureVector
     */
    public static List<int[]> getFeatureVector(List<String[]> splittedSentenceList, HashSet<String> allWordSet) {
        List<int[]> featureVectorList = new ArrayList<int[]>();

        for(String[] splittedSentence : splittedSentenceList) {
            int length = splittedSentence.length;
            int[] vector = new int[ length ];

            for(int i=0; i<length; i++) {
                vector[i] = ( allWordSet.contains(splittedSentence[i]) ) ? 1 : 0;
            }
            featureVectorList.add(vector);
        }

        return featureVectorList;
    }

    public static void main(String[] args) throws Exception{
        XmlParserByXpath xp = new XmlParserByXpath("tempest/data/medline00015.xml");
        List<String> stringList = xp.parseXmlByJavaxXpath();
        List<String[]> splittedSentenceList = splitSentence(stringList);
        HashSet<String> allWordSet = getAllWordSet(splittedSentenceList);
        for(int[] featureVector : getFeatureVector(splittedSentenceList, allWordSet)) {
            System.out.println(featureVector);
            /*
            for (int i=0; i<featureVector.length; i++){
                System.out.println(featureVector[i]);
            }
            */
        }
    }
}
