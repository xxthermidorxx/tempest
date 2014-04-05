package tempest.bolt;

import java.io.*;
import java.util.Map;

import tempest.org.msgpack.MessagePack;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseBasicBolt {

    protected MessagePack _msgpack;

    /**
     * constructor
     * @param void
     */
    public SplitSentenceBolt() {
        _msgpack = new MessagePack();
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String sentence = tuple.getString(0);
        String[] splittedSentence = sentence.split("[\\s]+");

        byte[] serializedSplittedSentence = {};
        try {
            serializedSplittedSentence = _msgpack.write(splittedSentence);
        } catch(IOException e) {
            e.printStackTrace();
        }

        /** HACK: 警告をなくすためにObjectでキャスト */
        collector.emit( new Values( (Object)serializedSplittedSentence ) );
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
