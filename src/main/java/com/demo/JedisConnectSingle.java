package com.demo;

import redis.clients.jedis.*;

import java.util.*;

/**
 * 使用jedis连接单节点redis
 */
public class JedisConnectSingle {

    public static void main(String[] args) {
        // 连接单机redis
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // String 类型
        jedis.set("testString", "123");
        System.out.println(jedis.get("testString"));
        jedis.append("testString", "456");
        System.out.println(jedis.get("testString"));
        jedis.del("testString");
        jedis.mset("testString", "zhangsan", "sex", "男", "age", "18");
        jedis.incr("age");
        System.out.println(jedis.mget("username", "sex", "age"));

        // list类型
        jedis.lpush("testList", "item1", "item2", "item3");
        System.out.println(jedis.lrange("testList", 0, -1));
        System.out.println(jedis.lrange("testList", 0, -1));
        jedis.del("testList");
        jedis.rpush("testList", "item4", "item5", "item6");
        System.out.println(jedis.lrange("testList", 0, -1));
        jedis.del("testList");
        // hash 类型
        Map<String, String> person = new HashMap<>();
        person.put("name", "李四");
        person.put("sex", "男");
        person.put("age", "28");
        jedis.hmset("person", person);
        System.out.println(jedis.hmget("person", "name", "sex", "age"));
        jedis.hdel("person", "age");
        System.out.println(jedis.hmget("person", "age"));
        System.out.println(jedis.hlen("person"));
        System.out.println(jedis.exists("person"));
        System.out.println(jedis.hkeys("person"));
        System.out.println(jedis.hvals("person"));
        System.out.println(jedis.hgetAll("person"));
        // set 类型
        jedis.sadd("users", "张三","李四","张三","王五");
        System.out.println(jedis.scard("users"));
        System.out.println(jedis.smembers("users"));
        jedis.srem("users", "张三");
        System.out.println(jedis.smembers("users"));
        System.out.println(jedis.sismember("users", "李四"));
        System.out.println(jedis.srandmember("users"));

        // 关闭redis
        jedis.close();
    }
}
