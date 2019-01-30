package DatabasePool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.io.Serializable;
/**
 * Redis连接池
 *
 * */
public class RedisPoolUtils implements Serializable {
    private static JedisPool pool;
    /**
     * 初始化连接池
     * Created by hky on 2018/12/10.
     * */

    static {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(1000);
            pool = new JedisPool(jedisPoolConfig,"slave2.hadoop",6379);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if ( pool != null) {
                Jedis resource = pool.getResource();
                //选择Redis数据库（例如db5）
                //resource.select(5);
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            pool.returnResourceObject(jedis);
        }
    }
}
