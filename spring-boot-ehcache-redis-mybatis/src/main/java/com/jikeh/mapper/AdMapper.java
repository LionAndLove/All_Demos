package com.jikeh.mapper;

import com.jikeh.model.Ad;

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
