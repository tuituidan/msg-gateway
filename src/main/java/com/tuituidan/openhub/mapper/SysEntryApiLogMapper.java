package com.tuituidan.openhub.mapper;

import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.vo.SysEntryApiLogView;
import com.tuituidan.tresdin.mybatis.mapper.BaseExtMapper;
import java.util.List;

/**
 * SysEntryApiLogMapper.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-08
 */
public interface SysEntryApiLogMapper extends BaseExtMapper<SysEntryApiLog> {

    /**
     * selectList
     *
     * @param search search
     * @return List
     */
    List<SysEntryApiLogView> selectList(SysEntryApiLog search);
}
