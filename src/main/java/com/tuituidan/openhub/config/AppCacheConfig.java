package com.tuituidan.openhub.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tuituidan.openhub.bean.vo.SysAppView;
import com.tuituidan.openhub.bean.vo.SysEntryApiView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppCacheConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-15
 */
@Configuration
public class AppCacheConfig {

    /**
     * appViewCache
     *
     * @return Cache
     */
    @Bean
    public Cache<Long, SysAppView> appViewCache() {
        return Caffeine.newBuilder().build();
    }

    /**
     * entryApiViewCache
     *
     * @return Cache
     */
    @Bean
    public Cache<Long, SysEntryApiView> entryApiViewCache() {
        return Caffeine.newBuilder().build();
    }

}
