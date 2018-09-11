/**
 * 极客慧 www.jikeh.cn
 * 头条号：https://www.toutiao.com/c/user/52045142300/
 */

package com.jikeh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
@SpringBootApplication
@EnableCaching
public class SpringBootMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMain.class, args);
    }

}
