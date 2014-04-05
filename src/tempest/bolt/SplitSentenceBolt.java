package tempest.bolt;

import java.io.*;
import java.util.Map;

import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseRichBolt {

    @Override
    public void excute(Tuple tuple, BasicOutputCollector collector) {
        String sentence = tuple.getString(0);
        String[] splittedSentence = sentence.split("[\\s]+");
        collector.emit(new Values(splittedSentence));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("splitted_sentence"));
    }
}
