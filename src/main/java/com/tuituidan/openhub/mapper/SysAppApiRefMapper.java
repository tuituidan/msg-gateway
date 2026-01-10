package com.tuituidan.openhub.mapper;

import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.bean.entity.SysAppApiRef;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * SysEntryApiMapper.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
public interface SysAppApiRefMapper extends Mapper<SysAppApiRef>, InsertListMapper<SysAppApiRef> {

    /**
     * selectAppsByApiId.
     *
     * @param apiId apiId
     * @return List
     */
    @Select("select * from sys_app where id in (select app_id from sys_app_api_ref where entry_api_id = #{apiId})")
    List<SysApp> selectAppsByApiId(@Param("apiId") Long apiId);
}
