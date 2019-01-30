package storm.Bolt;

import DatabasePool.RedisPoolUtils;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import entity.UserCount;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import java.util.Map;
/***
 *
 *
 * 在线用户数 bolt
 * Created by hky on 2018/12/11.
 */
public class UserCount_Bolt  implements IBasicBolt {
    Jedis redis;

    public void prepare(Map map, TopologyContext topologyContext) {
        redis = RedisPoolUtils.getJedis();
    }

    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String keys = tuple.getString(0); //日志名称
        String values = tuple.getString(1); //日志数据
        String data[] = values.split(",");
        if(keys.equals("tbl_ori_usercount")){
            String Record_time = data[1];
            String UserCount = data[2];
            if(Integer.parseInt(UserCount) < 1000000){
                //过滤数据中每小时统计的求和数据，我们界面实时展示，所以无需此数据
                String id = "4";
                UserCount uc = new UserCount(Record_time,UserCount,id);
                //redis.rpush("tbl_ori_usercount", JSON.toJSONString(uc));
                writeRedis("tbl_ori_usercount",JSON.toJSONString(uc),500);
            }
        }
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    private void writeRedis(String redisKey,String dataJson, int n){
        redis.rpush(redisKey, dataJson);
        Long keyLen=redis.llen(redisKey);
        if (n==0){
            return;
        }
        else if(keyLen>n){
            int i=(int) (keyLen-n);
            for(;i>0;i--){
                redis.lpop(redisKey);
            }
        }
    }
}
