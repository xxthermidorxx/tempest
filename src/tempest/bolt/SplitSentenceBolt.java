package tempest.bolt;

import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String sentence = tuple.getString(0);
        List<String> splittedSentence = Arrays.asList( sentence.split("[\\s|\\.|\\,|\\(|\\)|\\{|\\}]+") );
        splittedSentence = new ArrayList<>( splittedSentence );

        ListIterator<String> listitr = splittedSentence.listIterator();
        while (listitr.hasNext()) {
            String word = listitr.next();
            listitr.set( word.toLowerCase() );
            if (word.length() < 4) listitr.remove();
        }

        /** HACK: 警告をなくすためにObjectでキャスト */
        collector.emit( new Values(splittedSentence) );
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word_list"));
    }
}
