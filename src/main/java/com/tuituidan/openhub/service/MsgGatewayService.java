package com.tuituidan.openhub.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tuituidan.openhub.bean.dto.HttpRequestDto;
import com.tuituidan.openhub.bean.entity.SysEntryApiLog;
import com.tuituidan.openhub.bean.vo.AjaxResult;
import com.tuituidan.openhub.bean.vo.SysEntryApiView;
import com.tuituidan.openhub.consts.EntryStatusEnum;
import com.tuituidan.openhub.mapper.SysEntryApiLogMapper;
import com.tuituidan.openhub.util.HttpAuthUtils;
import com.tuituidan.tresdin.mybatis.util.SnowFlake;
import com.tuituidan.tresdin.util.ExpParserUtils;
import com.tuituidan.tresdin.util.RequestUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MsgGatewayService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Service
@Slf4j
public class MsgGatewayService {

    @Resource
    private SysEntryApiLogMapper sysEntryApiLogMapper;


    @Resource
    private CacheService cacheService;


    @ResponseBody
    public AjaxResult<String> msgGateway() {
        HttpRequestDto httpRequest = getHttpRequest();
        List<SysEntryApiView> apiList = cacheService.getEntryApiViewCache().asMap().values().stream()
                .filter(it -> Objects.equals(httpRequest.getUrl(), it.getPath())).collect(Collectors.toList());
        List<SysEntryApiView> expApiList = new ArrayList<>();
        SysEntryApiView defEntryApi = null;
        for (SysEntryApiView item : apiList) {
            if (StringUtils.isBlank(item.getTypeExp())) {
                defEntryApi = item;
                continue;
            }
            if (checkTypeExp(item.getTypeExp(), httpRequest)) {
                expApiList.add(item);
            }
        }
        if (CollectionUtils.isEmpty(expApiList) && defEntryApi != null) {
            expApiList.add(defEntryApi);
        }
        if (CollectionUtils.isEmpty(expApiList)) {
            insertEntryApiLog(null, httpRequest, EntryStatusEnum.UNKNOWN);
            return AjaxResult.success();
        }
        List<SysEntryApiView> validApiList = new ArrayList<>();
        for (SysEntryApiView item : expApiList) {
            if (authCheck(item, httpRequest)) {
                validApiList.add(item);
            } else {
                insertEntryApiLog(item, httpRequest, EntryStatusEnum.UN_AUTH);
            }
        }
        for (SysEntryApiView item : validApiList) {
            Long logId = insertEntryApiLog(item, httpRequest, EntryStatusEnum.SUCCESS);
            // todo try to execute handler
        }
        return AjaxResult.success();
    }

    private HttpRequestDto getHttpRequest() {
        HttpRequestDto requestDto = new HttpRequestDto();
        HttpServletRequest request = RequestUtils.getRequest();
        requestDto.setUrl(request.getRequestURI());
        String body;
        try (InputStream in = request.getInputStream()) {
            body = IOUtils.toString(in, request.getCharacterEncoding());
            Assert.hasText(body, "请求体不能为空");
        } catch (IOException e) {
            throw new IllegalArgumentException("请求体数据读取失败");
        }
        Assert.isTrue(JSON.isValid(body), "请求体仅支持json类型");
        // 压缩一下
        requestDto.setBody(JSON.isValidArray(body) ? JSON.parseArray(body) : JSON.parseObject(body));
        requestDto.setClientIp(RequestUtils.getClientIp());
        requestDto.setHeaders(new HashMap<>());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement().toLowerCase();
            requestDto.getHeaders().put(headerName, request.getHeader(headerName));
        }
        return requestDto;
    }

    private boolean checkTypeExp(String exp, HttpRequestDto httpRequest) {
        if (httpRequest.getBody().getClass().equals(JSONObject.class)) {
            return ExpParserUtils.evaluate(exp, (JSONObject) httpRequest.getBody());
        }
        JSONArray dataList = (JSONArray) httpRequest.getBody();
        boolean result = true;
        for (int i = 0; i < dataList.size(); i++) {
            result = result & ExpParserUtils.evaluate(exp, dataList.getJSONObject(i));
        }
        return result;
    }

    private boolean authCheck(SysEntryApiView entryApi, HttpRequestDto httpRequest) {
        try {
            HttpAuthUtils.checkHttpAuth(httpRequest.getHeaders(), entryApi.getHttpAuth());
            return true;
        } catch (Exception ex) {
            log.error("auth check failed", ex);
            return false;
        }
    }

    private Long insertEntryApiLog(SysEntryApiView entryApi, HttpRequestDto httpRequest,
            EntryStatusEnum statusEnum) {
        SysEntryApiLog log = new SysEntryApiLog()
                .setId(SnowFlake.newId())
                .setPath(httpRequest.getUrl())
                .setClientIp(httpRequest.getClientIp())
                .setHeaders(httpRequest.getHeaders().entrySet().stream()
                        .map(it -> it.getKey() + ":" + it.getValue()).collect(Collectors.joining("\n")))
                .setBody(JSON.toJSONString(httpRequest.getBody()))
                .setStatus(statusEnum.getCode());
        if (entryApi != null) {
            log.setEntryApiId(entryApi.getId());
        }
        sysEntryApiLogMapper.insert(log);
        return log.getId();
    }

    public Method getMethod() {
        try {
            return this.getClass().getMethod("msgGateway");
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException("Method not found");
        }
    }

}
