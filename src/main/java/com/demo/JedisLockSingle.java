package com.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 */
public class JedisLockSingle {
    // 获取锁
    public static boolean tryLock_with_set(String key, String value, int seconds) {
        boolean res = false;
        try{
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            Object obj = jedis.set(key, value, new SetParams().nx().ex(seconds));
            res = "OK".equals(obj);
            jedis.close();
        } catch (Exception e) {
            System.out.println("获取锁发生异常：" + value);
        }
        return res;
    }

    // 释放锁
    public static boolean releaseLock_with_lua(String key,String value) {
        boolean res = false;
        try {
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del',KEYS[1]) else return 0 end";
            res = jedis.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value)).equals(1L);
            jedis.close();
        } catch (Exception e) {
            System.out.println("释放锁发生异常：" + value);
        }
        return res;
    }

    /**
     * 测试
     */
    public static void test() {
        String key = "LOCK";
        String value = UUID.randomUUID().toString() + ":" + Thread.currentThread().getId();
        System.out.println("获取锁：" + value);
        boolean flag = tryLock_with_set(key, value, 10);
        if(flag) {
            System.out.println(Thread.currentThread().getId() + "获取到锁");
            try{
                // DO SOMETHING
                System.out.println(Thread.currentThread().getId() + "处理业务");
                Thread.sleep(3000);
            } catch (Exception e) {}
            finally {
                System.out.println(Thread.currentThread().getId() + "释放锁");
                releaseLock_with_lua(key, value);
            }
        } else {
            System.out.println(Thread.currentThread().getId() + "未获取到锁");
        }
    }

    public static void main(String[] args) {
        new Thread(() -> test()).start();
        new Thread(() -> test()).start();
        new Thread(() -> test()).start();
        new Thread(() -> test()).start();
//        new Thread(() -> test()).start();
    }
}
