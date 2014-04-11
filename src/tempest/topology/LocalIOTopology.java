package tempest.topology;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import tempest.spout.ReadFromLocalFileSpout;
import tempest.bolt.WriteToLocalFileBolt;

public class LocalIOTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new ReadFromLocalFileSpout(), 1);
        builder.setBolt("split", new WriteToLocalFileBolt(), 1).shuffleGrouping("spout");

        Config conf = new Config();
        conf.setDebug(true);

        conf.setNumWorkers(3);
        if (args != null && args.length > 0) {
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("io", conf, builder.createTopology());
            Thread.sleep(10000);
            cluster.shutdown();
        }
    }
}
