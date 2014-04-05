package tempest.bolt;

import java.io.*;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import tempest.org.msgpack.MessagePack;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExtractFeatureVectorBolt extends BaseBasicBolt {

    protected HashSet<String> _allWordSet;
    protected MessagePack _msgpack;

    /**
     * constructor
     * @param void
     */
    public ExtractFeatureVectorBolt() {
        _allWordSet = new HashSet<String>();
        _msgpack = new MessagePack();
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
        byte[] serializedSplittedSentence = tuple.getBinary(0);
        String[] splittedSentence = {};
        try {
            splittedSentence = _msgpack.read(serializedSplittedSentence, String[].class);
        } catch(IOException e) {
            e.printStackTrace();
        }
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
