package com.demo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class SpringBootConnect {
    @Test
    public void init() {

        //set 集合,用来装集群的ip 和端口的
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.20.101", 6379));
        nodes.add(new HostAndPort("192.168.20.101", 6380));
        nodes.add(new HostAndPort("192.168.20.102", 6379));
        nodes.add(new HostAndPort("192.168.20.102", 6380));
        nodes.add(new HostAndPort("192.168.20.103", 6379));
        nodes.add(new HostAndPort("192.168.20.103", 6380));
        // Jedis连接池配置
        JedisCluster jedis = new JedisCluster(nodes);
        //执行JedisCluster对象中的方法，方法和redis一一对应。

        // String 类型
        jedis.set("testString", "123");
        System.out.println(jedis.get("testString"));
        jedis.append("testString", "456");
        System.out.println(jedis.get("testString"));
        jedis.del("testString");
//        jedis.mset("testString", "zhangsan", "sex", "男", "age", "18");
//        jedis.incr("age");
//        System.out.println(jedis.mget("username", "sex", "age"));
        // list类型
        jedis.lpush("testList", "item1", "item2", "item3");
        System.out.println(jedis.lrange("testList", 0, -1));
        System.out.println(jedis.lrange("testList", 0, -1));
        jedis.del("testList");
        jedis.rpush("testList", "item4", "item5", "item6");
        System.out.println(jedis.lrange("testList", 0, -1));
        jedis.del("testList");
        // hash 类型
        System.out.println("---------------------- hash");
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

        jedis.close();

    }
}
