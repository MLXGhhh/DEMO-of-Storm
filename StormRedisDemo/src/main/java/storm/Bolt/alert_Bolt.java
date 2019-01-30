package storm.Bolt;
import DatabasePool.RedisPoolUtils;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import entity.alert_ipv4_realtime;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/**
 *
 * 安全告警Bolt处理
 * Created by hky on 2018/12/10.
 *
 * */
public class alert_Bolt implements IBasicBolt {
    Jedis redis;
    public void prepare(Map map, TopologyContext topologyContext) {
        redis = RedisPoolUtils.getJedis();
    }
    //将时间戳转化成时间格式
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    //将数据中IP的Long长整数形式转化成xx.xx.xxx.xx的形式
    public static String longToIP(long longIp) {
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

    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String keys = tuple.getString(0); //日志名称
        String values = tuple.getString(1); //日志数据
        String data[] = values.split(",");
        //安全告警日志
        if(keys.equals("tbl_ori_alert_ipv4_realtime")){
            String Record_Time = stampToDate(data[0]);  //流记录时间
            String Source_IP = longToIP(Long.parseLong(data[4])); //源地址
            String Source_Port = data[5];  //端口号
            String Destination_IP = longToIP(Long.parseLong(data[6]));  //目的地址
            String Destination_Port = data[7];  //目的端口
            String Protocol = data[8]; //协议类型
            String Signature = data[13]; //告警信息
            String Category = data[14]; // 告警类型
            String Severity = data[16];  //告警级别
            String id = "1";
            alert_ipv4_realtime ar = new alert_ipv4_realtime(Record_Time,Source_IP,Source_Port,
                    Destination_IP,Destination_Port,Protocol,Signature,Category,Severity,id);
            writeRedis("tbl_ori_alert_ipv4_realtime", JSON.toJSONString(ar),1000);
            basicOutputCollector.emit();
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
        //向redis的key中追加value
        redis.rpush(redisKey, dataJson);
        //检查key的长度
        Long keyLen=redis.llen(redisKey);
        if (n==0){
            return;
        }
        //若key的长度超过n，那么就删除头部元素
        else if(keyLen>n){
            int i=(int) (keyLen-n);
            for(;i>0;i--){
                redis.lpop(redisKey);
            }
        }
    }
}
