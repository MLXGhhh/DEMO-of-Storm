package storm.Bolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import entity.ipv4_realtime;
import DatabasePool.RedisPoolUtils;
import redis.clients.jedis.Jedis;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
/***
 *
 *IPv4实时数据流 bolt
 * Created by hky on 2018/12/7.
 */
public class ipv4_realtime_Bolt implements IBasicBolt {
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
        //ipv4实时流
        if(keys.equals("tbl_ori_flow_ipv4_realtime")){
            if(data.length == 29) {
                //判断协议类型 6位TCP 17为UDP
                if(data[6].equals("6")){
                    data[6] = "TCP";

                }
                if(data[6].equals("17")){
                    data[6] = "UDP";
                }
                String Time = stampToDate(data[15]);
                String Source_iP = longToIP(Long.parseLong(data[2]));
                String Source_port = data[4];
                String Destination_iP = longToIP(Long.parseLong(data[3]));
                String Destination_port = data[5];
                String Protocols = data[6];
                String id = "3";
                ipv4_realtime ir = new ipv4_realtime(Time,Source_iP,Source_port,Destination_iP,
                        Destination_port,Protocols,id);
                writeRedis("tbl_ori_flow_ipv4_realtime", JSON.toJSONString(ir),1000);
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
