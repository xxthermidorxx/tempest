package tempest.bolt;

import java.io.*;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExtractFeatureVectorBolt extends BaseBasicBolt {

    protected HashSet<String> _allWordSet;

    /**
     * constructor
     * @param void
     */
    public ExtractFeatureVectorBolt() {
        _allWordSet = new HashSet<String>();
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
     * @param String[] splitted sentence
     * @return int[] featureVector
     */
    private int[] __getFeatureVector(String[] splittedSentence) {
        int length = splittedSentence.length;
        int[] featureVector = new int[ length ];
        for(int i=0; i<length; i++) {
            featureVector[i] = ( _allWordSet.contains(splittedSentence[i]) ) ? 1 : 0;
        }
        return featureVector;
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String[] splittedSentence = (String[])tuple.getValue(0);
        __addWordToWordSet(splittedSentence);
        int[] featureVector = __getFeatureVector(splittedSentence);
        System.out.println(featureVector);

        collector.emit(new Values(featureVector, _allWordSet));
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
