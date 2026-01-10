package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.entity.SysPushLog;
import com.tuituidan.openhub.bean.vo.SysEntryApiLogView;
import com.tuituidan.openhub.bean.vo.SysPushLogView;
import com.tuituidan.openhub.service.SysLogService;
import com.tuituidan.tresdin.consts.TresdinConsts;
import com.tuituidan.tresdin.datatranslate.annotation.DataTranslate;
import com.tuituidan.tresdin.mybatis.bean.PageParam;
import com.tuituidan.tresdin.page.PageData;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SysEntryApiLogController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/10
 */
@RestController
@RequestMapping(TresdinConsts.API_V1 + "/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/api/page")
    @DataTranslate
    public PageData<List<SysEntryApiLogView>> selectApiLogPage(PageParam pageParam, SysEntryApiLog search) {
        return sysLogService.selectApiLogPage(pageParam, search);
    }

    @GetMapping("/api_log/{apiLogId}/push_log")
    @DataTranslate
    public List<SysPushLogView> selectPushLogByAppId(@PathVariable Long apiLogId) {
        return sysLogService.selectPushLogByAppId(apiLogId);
    }

    @GetMapping("/push/page")
    @DataTranslate
    public PageData<List<SysPushLogView>> selectPushLogPage(PageParam pageParam, SysPushLog search) {
        return sysLogService.selectPushLogPage(pageParam, search);
    }

}
