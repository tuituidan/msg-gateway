package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.entity.SysPushLog;
import com.tuituidan.openhub.bean.vo.SysEntryApiLogView;
import com.tuituidan.openhub.bean.vo.SysPushLogView;
import com.tuituidan.openhub.mapper.SysEntryApiLogMapper;
import com.tuituidan.openhub.mapper.SysPushLogMapper;
import com.tuituidan.tresdin.mybatis.QueryHelper;
import com.tuituidan.tresdin.mybatis.bean.PageParam;
import com.tuituidan.tresdin.page.PageData;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import java.util.stream.Collectors;
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

    @Resource
    private SysPushLogMapper sysPushLogMapper;

    public PageData<List<SysEntryApiLogView>> selectApiLogPage(PageParam pageParam, SysEntryApiLog search) {
        PageData<List<SysEntryApiLog>> pageData = QueryHelper.queryPage(pageParam.getOffset(),
                pageParam.getLimit(), () -> {
                    QueryHelper.orderBy(pageParam.getSort(), SysEntryApiLog.class);
                    return sysEntryApiLogMapper.select(search);
                });
        return QueryHelper.mapPage(pageData, log -> BeanExtUtils.convert(log, SysEntryApiLogView::new));
    }

    public List<SysPushLogView> selectPushLogByAppId(Long apiLogId) {
        return sysPushLogMapper.select(new SysPushLog().setLogId(apiLogId))
                .stream().map(it -> BeanExtUtils.convert(it, SysPushLogView::new))
                .collect(Collectors.toList());
    }

    public PageData<List<SysPushLogView>> selectPushLogPage(PageParam pageParam, SysPushLog search) {
        PageData<List<SysPushLog>> pageData = QueryHelper.queryPage(pageParam.getOffset(),
                pageParam.getLimit(), () -> {
                    QueryHelper.orderBy(pageParam.getSort(), SysPushLog.class);
                    return sysPushLogMapper.select(search);
                });
        return QueryHelper.mapPage(pageData, log -> BeanExtUtils.convert(log, SysPushLogView::new));
    }

}
