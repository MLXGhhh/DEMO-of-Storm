package storm.Bolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class SplitBolt implements IBasicBolt {


     public void prepare(Map map, TopologyContext topologyContext) {

     }


     public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String KafkaMessage = tuple.getString(0);
        String str[] = KafkaMessage.split(";");
        String keys = str[0];//日志名称
        String values = str[1];//日志内容
        basicOutputCollector.emit(new Values(keys,values));
     }

     public void cleanup() {

     }

     public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
         outputFieldsDeclarer.declare(new Fields("name","data"));
     }

     public Map<String, Object> getComponentConfiguration() {

         return null;
     }
}
