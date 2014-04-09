package tempest.bolt;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExtractFeatureVectorBolt extends BaseBasicBolt {

    protected LinkedHashSet<String> _allWordSet;

    /**
     * constructor
     * @param void
     */
    public ExtractFeatureVectorBolt() {
        _allWordSet = new LinkedHashSet<String>();
    }

    /**
     * add word to _allWordSet
     * @param String[] splitted sentence
     * @return void
     */
    private void __addWordToWordSet(String[] splittedSentence) {
        for(String word : splittedSentence) {
            _allWordSet.add(word);
        }
    }

    /**
     * get the feature vector
     * @param List<String> splitted sentence
     * @return HashMap<String, Integer> featureVector
     */
    private HashMap<String, Integer> __getFeatureVector(List<String> splittedSentenceList) {
        HashMap<String, Integer> featureVector = new HashMap<>();
        for(String word : new ArrayList<>(_allWordSet)) {
            int isExist = splittedSentenceList.contains(word) ? 1 : 0;
            featureVector.put(word, isExist);
        }

        return featureVector;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String[] splittedSentence = (String[]) tuple.getValue(0);
        __addWordToWordSet(splittedSentence);
        HashMap<String, Integer> featureVector = __getFeatureVector( Arrays.asList(splittedSentence));
        System.out.println(featureVector);

        collector.emit(new Values(featureVector));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("feature_vector", "all_word_set"));
    }
}
