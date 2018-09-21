package com.jikeh.mapper;

import com.jikeh.model.Ad;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author jikeh
 */
public interface AdMapper {

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    Ad selectByPrimaryKey(Long id);

    /**
     * 条件查询
     *
     * @param ad
     * @return
     */
    List<Ad> selectByAd(Ad ad, RowBounds rowBounds);

    /**
     * 查询全部数据
     *
     * @return
     */
    List<Ad> selectAll();

    /**
     * 新增
     *
     * @param ad
     * @return
     */
    int insert(Ad ad);

    /**
     * 根据主键更新
     *
     * @param ad
     * @return
     */
    int updateById(Ad ad);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

}
