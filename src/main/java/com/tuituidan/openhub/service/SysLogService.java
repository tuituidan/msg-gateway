package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.vo.SysEntryApiLogView;
import com.tuituidan.openhub.mapper.SysEntryApiLogMapper;
import com.tuituidan.tresdin.mybatis.QueryHelper;
import com.tuituidan.tresdin.mybatis.bean.PageParam;
import com.tuituidan.tresdin.page.PageData;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * SysEntryApiLogService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/10
 */
@Service
public class SysLogService {

    @Resource
    private SysEntryApiLogMapper sysEntryApiLogMapper;

    public PageData<List<SysEntryApiLogView>> selectApiLogPage(PageParam pageParam, SysEntryApiLog search) {
        return QueryHelper.queryPage(pageParam.getOffset(),
                pageParam.getLimit(), () -> {
                    QueryHelper.orderBy(pageParam.getSort(), SysEntryApiLog.class);
                    return sysEntryApiLogMapper.selectList(search);
                });
    }

    public SysEntryApiLogView selectApiLogById(Long id) {
        return BeanExtUtils.convert(sysEntryApiLogMapper.selectByPrimaryKey(id), SysEntryApiLogView::new);
    }

}
