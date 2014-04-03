package tempest.bolt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitSentenceBolt extends BaseRichBolt {

    private String _a;

    @Override
    public void excute(Tuple tuple, BasicOutputCollector collector) {
    }

    @Override
    public void cleanup() {
    }
}
