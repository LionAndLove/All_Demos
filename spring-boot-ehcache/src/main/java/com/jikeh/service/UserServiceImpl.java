package com.jikeh.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl {

    @Cacheable(value = "users", key = "#p0")
    public String getUserNameByCache(Long userId){
        //首次调用，才会打印这句话; 在缓存有效时间内，接下来的调用就不会再打印这句话了;
        System.out.printf("没有执行缓存\n");
        return userId+"极客慧";
    }

}
