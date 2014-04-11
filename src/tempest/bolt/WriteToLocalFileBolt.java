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
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class WriteToLocalFileBolt extends BaseBasicBolt {

    //private static final Logger log = LoggerFactory.getLogger(WriteToLocalFileBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String text = tuple.getString(0);
        int spoutId = tuple.getInteger(1);
        collector.emit(new Values(text, spoutId));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word_list", "spoutID"));
    }
}
