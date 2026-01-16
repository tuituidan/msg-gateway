package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.SysEntryApiType;
import com.tuituidan.openhub.mapper.SysEntryApiTypeMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * SysEntryApiTypeService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-14
 */
@Service
public class SysEntryApiTypeService {

    @Resource
    private SysEntryApiTypeMapper sysEntryApiTypeMapper;

    public List<SysEntryApiType> selectList() {
        return sysEntryApiTypeMapper.selectAll();
    }

    public void save(Long id, String name) {
        if (id == null) {
            sysEntryApiTypeMapper.insertSelective(new SysEntryApiType().setName(name));
            return;
        }
        sysEntryApiTypeMapper.updateByPrimaryKeySelective(new SysEntryApiType().setId(id).setName(name));
    }

    public void delete(Long id) {
        sysEntryApiTypeMapper.deleteByPrimaryKey(id);
    }

}
