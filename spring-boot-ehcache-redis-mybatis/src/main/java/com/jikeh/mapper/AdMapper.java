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

}
