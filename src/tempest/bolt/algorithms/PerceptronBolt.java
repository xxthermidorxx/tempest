package tempest.algorithms;

import tempest.XmlParser.XmlParserByXpath;
import tempest.FeatureExtraction;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PerceptronBolt {

    private int _dataSize;
    private int _dimension;
    private int[] _weightVector;
    private int[] _categorized;
    private List<int[]> _featureVectorList;

    /**
     * constructor
     * @param List<int[]> featureVectorList
     * @return void
     */
    public Perceptron(List<int[]> featureVectorList) {
        _dataSize = featureVectorList.length();
        _dimension = featureVectorList.get(0).length;
        _featureVectorList = featureVectorList;
        _weightVector = new int[ _dimension ];

        Random rnd = new Random();
        int[] num = {-1, 1};
        for(int i=0; i<_dimension; i++) {
            _weightVector[i] = rnd.nextInt(10) - 5;
            _categorized[i] = num[ rnd.nextInt(1) ];
        }
    }

    /**
     * calculate the dot product of _weightVector and _featureVector[i]
     * @param void
     * @return int
     */
    public int[] calcDotProduct() {
        int[] dotProductArray = new int[_dataSize];
        int sum = 0;
        for(int i=0; i<_dataSize; i++) {
            int[] vector = _featureVectorList.get(i);
            for(int j=0; j<_dimension; j++) {
                sum += _weightVector[j] * vector[j];
            }
            dotProductArray[i] = sum;
        }
        return dotProductArray;
    }

    /**
     * if right nothing happens and if wrong fix the _weightVector
     * @param void
     * @return int
     */
    public void predict() {
        int loopCount = 0;
        while(loopCount < 1000) {
            int[] dotProductArray = calcDotProduct();

            int predictionMiss = 0;
            for(int i=0; i<dotProductArray.length; i++) {
                int prediction = (dotProductArray[i] > 0) ? 1 : -1;
                if(prediction != _categorized[i]) {
                    _weightVector += prediction * _weightVector[];    // TODO
                    predictionMiss++;
                }
            }
            if(predictionMiss == 0) break;
            loopCount++;
        }
        return;
    }

    public int[] getWeightVector() {
        return _weightVector;
    }

    public static void main(String[] args) throws Exception {
        XmlParserByXpath xp = new XmlParserByXpath("tempest/data/medline00015.xml");
        List<String> stringList = xp.parseXmlByJavaxXpath();
        List<String[]> splittedSentenceList = FeatureExtraction.splitSentence(stringList);
        HashSet<String> allWordSet = FeatureExtraction.getAllWordSet(splittedSentenceList);
        for(int[] featureVector : FeatureExtraction.getFeatureVector(splittedSentenceList, allWordSet)) {
            for (int i=0; i<featureVector.length; i++){
                System.out.println(featureVector[i]);
            }
        }
    }
}
