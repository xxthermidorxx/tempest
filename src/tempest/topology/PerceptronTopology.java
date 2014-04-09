package tempest.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import tempest.spout.XmlReadoutSpout;
import tempest.bolt.SplitSentenceBolt;
import tempest.bolt.ExtractFeatureVectorBolt;

public class PerceptronTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new XmlReadoutSpout(), 1);
        builder.setBolt("split", new SplitSentenceBolt(), 1).shuffleGrouping("spout");
        builder.setBolt("extract", new ExtractFeatureVectorBolt(), 1).shuffleGrouping("split");

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(3);
        if (args != null && args.length > 0) {
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("perceptron", conf, builder.createTopology());
            Thread.sleep(6000);
            cluster.shutdown();
        }
    }
}
