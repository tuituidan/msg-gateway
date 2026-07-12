package com.tuituidan.openhub.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.bean.vo.SysAppView;
import com.tuituidan.openhub.bean.vo.SysEntryApiView;
import com.tuituidan.openhub.mapper.SysAppMapper;
import com.tuituidan.openhub.mapper.SysEntryApiMapper;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import javax.annotation.Resource;
import lombok.Getter;
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
    private SysEntryApiMapper sysEntryApiMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SysEntryApi> entryApiList = sysEntryApiMapper.selectAll();
        for (SysEntryApi entryApi : entryApiList) {
            SysEntryApiView entryApiView = BeanExtUtils.convert(entryApi, SysEntryApiView::new);
            entryApiViewCache.put(entryApi.getId(), entryApiView);
        }
        List<SysApp> appList = sysAppMapper.selectAll();
        for (SysApp sysApp : appList) {
            appViewCache.put(sysApp.getId(), BeanExtUtils.convert(sysApp, SysAppView::new));
        }
    }

}
