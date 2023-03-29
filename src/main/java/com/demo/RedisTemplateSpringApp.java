package com.demo;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * 使用Spring RedisTemplate连接
 */
@Component
@SpringBootApplication
public class RedisTemplateSpringApp {

    public static void main(String[] args) {
        System.out.println("启动前");
        SpringApplication.run(RedisTemplateSpringApp.class, args);
        System.out.println("启动后");
    }

    @Autowired
    RedisTemplate redisTemplate;

    @PostConstruct
    public void Test() {
        // String 类型
        redisTemplate.opsForValue().set("Name", "Tom");
        Assert.assertEquals(redisTemplate.opsForValue().get("Name"), "Tom");
        redisTemplate.delete("Name");
        Assert.assertEquals(redisTemplate.opsForValue().get("Name"), null);
        // Hash类型
        redisTemplate.opsForHash().put("Hash", "name", "jim");
        redisTemplate.opsForHash().put("Hash", "age", 13);
        Assert.assertEquals(redisTemplate.opsForHash().get("Hash", "name"), "jim");
        Assert.assertEquals(redisTemplate.opsForHash().get("Hash", "age"), 13);
        redisTemplate.opsForHash().delete("Hash", "age");
        Assert.assertEquals(redisTemplate.opsForHash().get("Hash", "name"), "jim");
        Assert.assertEquals(redisTemplate.opsForHash().get("Hash", "age"), null);
        redisTemplate.delete("Hash");
        Assert.assertEquals(redisTemplate.opsForHash().get("Hash", "name"), null);
        // List类型
        redisTemplate.opsForList().rightPush("List", "1");
        redisTemplate.opsForList().rightPushAll("List", "2", "3");
        List list = redisTemplate.opsForList().range("List", 0, -1);
        Assert.assertEquals(list.size(), 3);
        Object item = redisTemplate.opsForList().rightPop("List");
        Assert.assertEquals("3", item);
        redisTemplate.delete("List");
        // Set类型
        redisTemplate.delete("Set");
        redisTemplate.opsForSet().add("Set", "1", "2", "3", "4");
        Assert.assertEquals(4, redisTemplate.opsForSet().size("Set").intValue());
        redisTemplate.opsForSet().remove("Set", "2");
        Assert.assertEquals(3, redisTemplate.opsForSet().size("Set").intValue());
        Object item2 = redisTemplate.opsForSet().pop("Set");
//        Assert.assertEquals("1", item2);
        Assert.assertEquals(2, redisTemplate.opsForSet().size("Set").intValue());
        redisTemplate.delete("Set");
        // ZSet类型
        redisTemplate.opsForZSet().add("ZSet", "1", 2);
        redisTemplate.opsForZSet().add("ZSet", "2", 1);
        redisTemplate.opsForZSet().add("ZSet", "3", 4);
        redisTemplate.opsForZSet().add("ZSet", "4", 3);
        Assert.assertEquals(4, redisTemplate.opsForZSet().size("ZSet").intValue());
        Set set = redisTemplate.opsForZSet().range("ZSet", 0, -1);
        System.out.println(set);    // [2, 1, 4, 3]  LinkedHashSet
        redisTemplate.delete("ZSet");
    }
}
