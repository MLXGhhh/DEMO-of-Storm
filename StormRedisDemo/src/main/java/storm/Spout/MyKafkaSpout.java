package storm.Spout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.Bolt.*;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * kafkaspout
 * Created by hky on 2018/12/07.
 *
 * */

public class MyKafkaSpout {
    public static void main(String[] args) {
        String topic = "ProbeFlowLog";
        ZkHosts zkHosts = new ZkHosts("159.226.16.151:2181,159.226.16.153:2181,159.226.16.155:2181");
        SpoutConfig spoutconfig = new SpoutConfig(zkHosts,topic,"/order","spout");
        List<String> zkServers = new ArrayList<String>();
        zkServers.add("159.226.16.151,159.226.16.153,159.226.16.155");
        spoutconfig.zkServers = zkServers;
        spoutconfig.zkPort = 2181;
        spoutconfig.socketTimeoutMs = 10000;
        //从kafka的最开始消费数据
        spoutconfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
        spoutconfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        //创建Topology
        TopologyBuilder mybulider = new TopologyBuilder();
        mybulider.setSpout("spout",new KafkaSpout(spoutconfig),1);

        mybulider.setBolt("SplitBolt",new SplitBolt(),1)
                .shuffleGrouping("spout");
        mybulider.setBolt("ipv4_RealTime_Bolt",new ipv4_realtime_Bolt(),1)
                .shuffleGrouping("SplitBolt");
        mybulider.setBolt("alert_ipv4_RealTime",new alert_Bolt(),1)
                .shuffleGrouping("SplitBolt");
        mybulider.setBolt("UserCount_Bolt",new UserCount_Bolt(),1)
                .shuffleGrouping("SplitBolt");
        mybulider.setBolt("alert_sourceip_Bolt",new alert_sourceip_Bolt(),1)
                .shuffleGrouping("SplitBolt");

        Config conf = new Config();
        if (args.length > 0){
            try{
                StormSubmitter.submitTopology(args[0],conf,mybulider.createTopology());
            } catch (Exception e){
                e.printStackTrace();
            }
        }else{
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("topology",conf,mybulider.createTopology());
        }
    }
}
