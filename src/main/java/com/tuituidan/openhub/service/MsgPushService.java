package com.tuituidan.openhub.service;

import com.alibaba.fastjson2.JSON;
import com.tuituidan.openhub.bean.dto.HttpRequestDto;
import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.entity.SysPushLog;
import com.tuituidan.openhub.bean.vo.SysAppView;
import com.tuituidan.openhub.consts.PushStatusEnum;
import com.tuituidan.openhub.mapper.SysEntryApiLogMapper;
import com.tuituidan.openhub.mapper.SysPushLogMapper;
import com.tuituidan.openhub.util.HttpAuthUtils;
import com.tuituidan.tresdin.util.ExpParserUtils;
import com.tuituidan.tresdin.util.thread.CompletableUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * MsgPushService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Service
@Slf4j
public class MsgPushService {

    @Resource
    private SysEntryApiLogMapper sysEntryApiLogMapper;

    @Resource
    private SysPushLogMapper sysPushLogMapper;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CacheService cacheService;

    /**
     * push.
     *
     * @param logId logId
     * @param httpRequest httpRequest
     */
    public void push(Long logId, List<SysAppView> appList, HttpRequestDto httpRequest) {
        if (appList.size() == 1) {
            pushToApp(logId, appList.get(0), JSON.toJSONString(httpRequest.getBody()));
            return;
        }
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (SysAppView sysApp : appList) {
            futures.add(CompletableUtils.runAsync(() -> pushToApp(logId, sysApp,
                            JSON.toJSONString(httpRequest.getBody())),
                    "push-pool").exceptionally(ex -> {
                log.error("数据日志推送异常", ex);
                return null;
            }));
        }
        CompletableUtils.waitAll(futures);
    }

    public void pushToApp(Long logId, SysAppView sysApp, String postData) {
        SysPushLog pushLog = new SysPushLog();
        long startTime = System.currentTimeMillis();
        pushLog.setAppId(sysApp.getId())
                .setPushTime(LocalDateTime.now())
                .setLogId(logId);
        pushToApp(pushLog, sysApp, postData);
        pushLog.setCostTime(System.currentTimeMillis() - startTime);
        pushLog.setPushTimes(1);
        sysPushLogMapper.insert(pushLog);
    }

    public void rePushToApp(Long id) {
        SysPushLog oldPushLog = sysPushLogMapper.selectByPrimaryKey(id);
        SysEntryApiLog apiLog = sysEntryApiLogMapper.selectByPrimaryKey(oldPushLog.getLogId());
        SysAppView appView = cacheService.getAppViewCache().getIfPresent(oldPushLog.getAppId());
        SysPushLog pushLog = new SysPushLog().setId(id).setPushTime(LocalDateTime.now());
        long startTime = System.currentTimeMillis();
        pushToApp(pushLog, appView, apiLog.getBody());
        pushLog.setCostTime(System.currentTimeMillis() - startTime);
        pushLog.setPushTimes(oldPushLog.getPushTimes() + 1);
        sysPushLogMapper.updateByPrimaryKeySelective(pushLog);
    }

    private void pushToApp(SysPushLog pushLog, SysAppView sysApp, String postData) {
        try {
            HttpHeaders httpHeaders = HttpAuthUtils.buildHttpHeaders(sysApp.getHttpAuth());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> response = restTemplate.exchange(sysApp.getUrl(), HttpMethod.POST,
                    new HttpEntity<>(postData, httpHeaders), String.class);
            pushLog.setResponse(StringUtils.truncate(StringUtils.defaultString(response.getBody(),
                    "无数据返回"), 4000));
            pushLog.setStatus(analysePushStatus(response, sysApp));
        } catch (Exception ex) {
            pushLog.setResponse(StringUtils.truncate(ExceptionUtils.getStackTrace(ex), 4000));
            pushLog.setStatus(PushStatusEnum.FAIL.getCode());
        }
    }

    private String analysePushStatus(ResponseEntity<String> response, SysAppView sysApp) {
        if (StringUtils.isBlank(sysApp.getResultExp())) {
            return response.getStatusCode().is2xxSuccessful()
                    ? PushStatusEnum.SUCCESS.getCode() : PushStatusEnum.FAIL.getCode();

        }
        if (StringUtils.isBlank(response.getBody())) {
            return PushStatusEnum.FAIL.getCode();
        }
        return ExpParserUtils.evaluate(sysApp.getResultExp(), JSON.parseObject(response.getBody()))
                ? PushStatusEnum.SUCCESS.getCode() : PushStatusEnum.FAIL.getCode();
    }

}
