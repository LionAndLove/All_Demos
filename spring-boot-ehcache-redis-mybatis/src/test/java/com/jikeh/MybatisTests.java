package com.jikeh;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.mapper.AdMapper;
import com.jikeh.model.Ad;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTests {

    private static final Log log = LogFactory.getLog(MybatisTests.class);

    @Autowired //注入sqlSessionFactory
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession=null;

    private SqlSession getSqlSession(){
        if(sqlSession==null){
            sqlSession=sqlSessionFactory.openSession();
        }
        return sqlSession;
    }

    @Test
    public void selectByPrimaryKey() {
        Ad ad = getSqlSession().getMapper(AdMapper.class).selectByPrimaryKey(1L);
        System.out.println(JSONObject.toJSONString(ad));
    }

    @Test
    public void testInsert() {
        Ad ad = new Ad();
        ad.setName("缓存穿透、缓存雪崩初探-redis+ehcache+mybatis缓存架构测试");
        ad.setDestinationUrl("https://www.toutiao.com/i6602586317362561544/");
        ad.setImgUrl("http://p1.pstatp.com/large/pgc-image/15372844054148ce4c8be55");
        System.out.println(getSqlSession().getMapper(AdMapper.class).insert(ad));
    }

    @Test
    public void updateById() {
        Ad ad = new Ad();
        ad.setId(3L);
        ad.setName("缓存架构之SpringBoot集成redis并结合ehcache实现二级缓存架构\n");
        ad.setDestinationUrl("https://www.toutiao.com/i6602245004314280451/");
        ad.setImgUrl("http://p3.pstatp.com/large/pgc-image/15372047674175e7582d1aa");
        System.out.println(getSqlSession().getMapper(AdMapper.class).updateById(ad));
    }

    @Test
    public void deleteById() {
        System.out.println(getSqlSession().getMapper(AdMapper.class).deleteById(1L));
    }


}
