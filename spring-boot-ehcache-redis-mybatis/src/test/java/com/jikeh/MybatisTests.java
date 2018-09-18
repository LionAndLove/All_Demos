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

}
