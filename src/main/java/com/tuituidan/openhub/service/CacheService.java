package com.tuituidan.openhub.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.bean.entity.SysAppApiRef;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.bean.vo.SysAppView;
import com.tuituidan.openhub.bean.vo.SysEntryApiView;
import com.tuituidan.openhub.mapper.SysAppApiRefMapper;
import com.tuituidan.openhub.mapper.SysAppMapper;
import com.tuituidan.openhub.mapper.SysEntryApiMapper;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * CacheService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-15
 */
@Service
public class CacheService implements ApplicationRunner {

    @Resource
    @Getter
    private Cache<Long, SysAppView> appViewCache;

    @Resource
    @Getter
    private Cache<Long, SysEntryApiView> entryApiViewCache;

    @Resource
    private SysAppMapper sysAppMapper;

    @Resource
    private SysAppApiRefMapper sysAppApiRefMapper;

    @Resource
    private SysEntryApiMapper sysEntryApiMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SysEntryApi> entryApiList = sysEntryApiMapper.selectAll();
        List<SysApp> appList = sysAppMapper.selectAll();
        Map<Long, List<Long>> apiRefMap = sysAppApiRefMapper.selectAll().stream()
                .collect(Collectors.groupingBy(SysAppApiRef::getEntryApiId,
                        Collectors.mapping(SysAppApiRef::getAppId, Collectors.toList())));
        for (SysEntryApi entryApi : entryApiList) {
            SysEntryApiView entryApiView = BeanExtUtils.convert(entryApi, SysEntryApiView::new);
            List<Long> appIds = apiRefMap.get(entryApi.getId());
            if (CollectionUtils.isNotEmpty(appIds)) {
                entryApiView.setAppList(appList.stream().filter(app -> appIds.contains(app.getId()))
                        .map(app -> BeanExtUtils.convert(app, SysAppView::new)).collect(Collectors.toList()));
            }
            entryApiViewCache.put(entryApi.getId(), entryApiView);
        }
        for (SysApp sysApp : appList) {
            appViewCache.put(sysApp.getId(), BeanExtUtils.convert(sysApp, SysAppView::new));
        }
    }

    public void refreshAppViewCache(Long appId) {
        appViewCache.invalidate(appId);
        SysApp sysApp = sysAppMapper.selectByPrimaryKey(appId);
        if (sysApp != null) {
            appViewCache.put(appId, BeanExtUtils.convert(sysApp, SysAppView::new));
        }
        List<Long> entryApiIds = entryApiViewCache.asMap().values().stream().filter(it -> {
            if (CollectionUtils.isEmpty(it.getAppList())) {
                return false;
            }
            return it.getAppList().stream().anyMatch(app -> Objects.equals(app.getId(), appId));
        }).map(SysEntryApiView::getId).collect(Collectors.toList());
        for (Long entryApiId : entryApiIds) {
            refreshEntryApiViewCache(entryApiId);
        }
    }

    public void refreshEntryApiViewCache(Long entryApiId) {
        entryApiViewCache.invalidate(entryApiId);
        SysEntryApi entryApi = sysEntryApiMapper.selectByPrimaryKey(entryApiId);
        if (entryApi == null) {
            return;
        }
        SysEntryApiView entryApiView = BeanExtUtils.convert(entryApi, SysEntryApiView::new);
        List<Long> appIds = sysAppApiRefMapper.select(new SysAppApiRef().setEntryApiId(entryApi.getId()))
                .stream().map(SysAppApiRef::getAppId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(appIds)) {
            entryApiView.setAppList(sysAppMapper.selectByIds(StringUtils.join(appIds, ",")).stream()
                    .map(app -> BeanExtUtils.convert(app, SysAppView::new)).collect(Collectors.toList()));
        }
        entryApiViewCache.put(entryApi.getId(), entryApiView);
    }

}
