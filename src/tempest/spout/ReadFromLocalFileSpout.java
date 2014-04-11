package tempest.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadFromLocalFileSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    List<String> _str_list;
    int _taskId;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _str_list = new ArrayList<String>( Arrays.asList("jnokmklm", "vadjvnlnknkl", "kojisovjnonko"));
        _taskId = context.getThisTaskId();
    }

    @Override
    public void nextTuple() {
        _collector.emit(new Values(_str_list.get(1), _taskId));
        Utils.sleep(100);
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word", "task_id"));
    }

}
