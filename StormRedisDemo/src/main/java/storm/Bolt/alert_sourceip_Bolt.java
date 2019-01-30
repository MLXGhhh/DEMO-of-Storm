package storm.Bolt;
import DatabasePool.RedisPoolUtils;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import entity.ipv4_alert_sourceip;
import redis.clients.jedis.Jedis;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/**
 * 告警源ip统计
 * Created by hky on 2018/12/14.
 * */
public class alert_sourceip_Bolt implements IBasicBolt {
    Jedis jedis;
    public static String longToIP(long longIp){
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public void prepare(Map map, TopologyContext topologyContext) {
        jedis = RedisPoolUtils.getJedis();
    }

    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String keys = tuple.getString(0); //日志名称
        String values = tuple.getString(1); //日志数据
        String data[] = values.split(",");
        if(keys.equals("tbl_ori_ipv4_alert_sourceip")) {
            String RecordTime = stampToDate(data[0]); //记录时间
            String SourceIP = longToIP(Long.parseLong(data[2])); //源IP
            String AttackWay = data[4];//攻击方向
            String AttackCount = data[5];//攻击数量
            String id = "2";
            ipv4_alert_sourceip as = new ipv4_alert_sourceip(RecordTime,SourceIP,AttackWay,AttackCount,id);
            //攻击方源ip
            writeRedis("Attacker_Sourceip", JSON.toJSONString(as),1000);
        }
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
    private void writeRedis(String redisKey,String dataJson, int n) {
        jedis.rpush(redisKey, dataJson);
        Long keyLen = jedis.llen(redisKey);
        if (n == 0) {
            return;
        } else if (keyLen > n) {
            int i = (int) (keyLen - n);
            for (; i > 0; i--) {
                jedis.lpop(redisKey);
            }
        }
    }
}
