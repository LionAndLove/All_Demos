package com.jikeh.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisManager {

    private static Logger logger = LoggerFactory.getLogger(RedisManager.class);
    private static JedisPoolConfig jedisPoolConfig;
    private static JedisPool pool = null;

    private RedisManager() {
        String host = "localhost";
        int port = 6379;
        jedisPoolConfig = new JedisPoolConfig();
        pool = new JedisPool(jedisPoolConfig, host, Integer.valueOf(port));
    }

    private static final RedisManager manager = new RedisManager();

    public static RedisManager getInstance() {
        return manager;
    }

    public synchronized Jedis getConnection() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();

        } catch (JedisConnectionException e) {
            logger.error(e.getMessage(), e);
            if (null != jedis) {
                pool.destroy();
                jedis = null;
            }
        }
        return jedis;
    }

    @SuppressWarnings("deprecation")
    public void closeConnection(Jedis jedis) {
        if (null != jedis) {
            try {
                pool.returnResource(jedis);
            } catch (Exception e) {
                if (pool != null) {
                    pool.returnBrokenResource(jedis);// 销毁连接
                }
                logger.error("redis Datasource：" + e);
            }
        }
    }

    /**
     * 自获取jedis并释放
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setStr(String key, String value) {
        boolean flag = true;
        Jedis jedis = pool.getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("", e);
            flag = false;
        } finally {
            jedis.close();
        }
        return flag;
    }

    /**
     * 自获取jedis并释放
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setStrWithExpire(String key, String value, int days) {
        boolean flag = true;
        Jedis jedis = pool.getResource();
        try {
            jedis.setex(key, days * 86400, value);
        } catch (Exception e) {
            logger.error("", e);
            flag = false;
        } finally {
            jedis.close();
        }
        return flag;
    }

    /**
     * 自获取jedis并释放，不存在返回null
     *
     * @param key
     * @param
     * @return
     */
    public String get(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            jedis.close();
        }
        return "";
    }

    public boolean exists(String redisKey) {
        Jedis jedis = pool.getResource();
        boolean flag = false;
        try {
            flag = jedis.exists(redisKey);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            jedis.close();
        }
        return flag;
    }

    public static void main(String[] args) {
        RedisManager rm = RedisManager.getInstance();

        System.out.print(System.currentTimeMillis());
        for (int index = 0; index < 10000; index++) {
            Jedis jedis = rm.getConnection();
            jedis.set("imp_ordersn_1_12345:20161009", "167956");
            rm.closeConnection(jedis);
        }

        System.out.print(System.currentTimeMillis());

        Jedis jedis = rm.getConnection();
        // 检测key是否存在
        logger.info(jedis.exists("a").toString());

        // 字符串读取测试
        logger.info(jedis.get("a"));

        rm.closeConnection(jedis);

    }

}
