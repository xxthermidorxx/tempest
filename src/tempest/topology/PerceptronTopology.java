package tempest.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

import tempest.spout.XmlReadoutSpout;
import tempest.bolt.SplitSentenceBolt;
import tempest.bolt.ExtractFeatureVectorBolt;

public class PerceptronTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new XmlReadoutSpout(), 5);
        builder.setBolt("split", new SplitSentenceBolt(), 3).shuffleGrouping("spout");
        builder.setBolt("extract", new ExtractFeatureVectorBolt(), 4).shuffleGrouping("split");

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(9);
        StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
    }
}
