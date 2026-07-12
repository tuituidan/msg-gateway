package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.SysEntryApiDto;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.mapper.SysEntryApiMapper;
import com.tuituidan.tresdin.mybatis.util.SnowFlake;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * SysEntryApiService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Service
public class SysEntryApiService implements ApplicationRunner {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private SysEntryApiMapper sysEntryApiMapper;

    @Resource
    private MsgGatewayService msgGatewayService;

    @Resource
    private CacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SysEntryApi> entryApis = sysEntryApiMapper.selectAll();
        for (SysEntryApi entryApi : entryApis) {
            registerMapping(entryApi);
        }
    }

    public List<SysEntryApi> selectList(SysEntryApi search) {
        return sysEntryApiMapper.select(search);
    }

    public void add(SysEntryApiDto entryApiDto) {
        checkRepeat(null, entryApiDto);
        SysEntryApi entryApi = BeanExtUtils.convert(entryApiDto, SysEntryApi::new);
        entryApi.setId(SnowFlake.newId());
        registerMapping(entryApi);
        sysEntryApiMapper.insert(entryApi);
        cacheService.getEntryApiViewCache().invalidate(entryApi.getId());
    }

    public void update(Long id, SysEntryApiDto entryApiDto) {
        checkRepeat(id, entryApiDto);
        SysEntryApi oldEntryApi = sysEntryApiMapper.selectByPrimaryKey(id);
        if (Objects.equals(oldEntryApi.getPath(), entryApiDto.getPath())) {
            BeanUtils.copyProperties(entryApiDto, oldEntryApi);
            sysEntryApiMapper.updateByPrimaryKeySelective(oldEntryApi);
            cacheService.getEntryApiViewCache().invalidate(oldEntryApi.getId());
            return;
        }
        // path不相同也应该判断是否有关联业务数据？
        requestMappingHandlerMapping.unregisterMapping(buildRequestMappingInfo(oldEntryApi));
        BeanUtils.copyProperties(entryApiDto, oldEntryApi);
        requestMappingHandlerMapping.registerMapping(buildRequestMappingInfo(oldEntryApi),
                msgGatewayService, msgGatewayService.getMethod());
        sysEntryApiMapper.updateByPrimaryKeySelective(oldEntryApi);
        cacheService.getEntryApiViewCache().invalidate(oldEntryApi.getId());
    }

    private void registerMapping(SysEntryApi entryApi) {
        RequestMappingInfo info = buildRequestMappingInfo(entryApi);
        requestMappingHandlerMapping.unregisterMapping(info);
        requestMappingHandlerMapping.registerMapping(info, msgGatewayService,
                msgGatewayService.getMethod());
    }

    private void checkRepeat(Long id, SysEntryApiDto entryApiDto) {
        List<SysEntryApi> extList = sysEntryApiMapper.select(new SysEntryApi().setPath(entryApiDto.getPath())
                .setTypeExp(entryApiDto.getTypeExp()));
        if (id != null) {
            extList = extList.stream().filter(item -> !Objects.equals(item.getId(), id)).collect(Collectors.toList());
        }
        Assert.isTrue(CollectionUtils.isEmpty(extList), "已经存在相同接口，相同表达式的接口");
    }

    public void delete(Long id) {
        SysEntryApi entryApi = sysEntryApiMapper.selectByPrimaryKey(id);
        // 需要判断有没有关联的业务数据，否则不允许删除
        requestMappingHandlerMapping.unregisterMapping(buildRequestMappingInfo(entryApi));
        sysEntryApiMapper.deleteByPrimaryKey(id);
        cacheService.getEntryApiViewCache().invalidate(id);
    }

    private RequestMappingInfo buildRequestMappingInfo(SysEntryApi entryApi) {
        return RequestMappingInfo
                .paths(entryApi.getPath()).methods(RequestMethod.POST)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .options(requestMappingHandlerMapping.getBuilderConfiguration())
                .build();
    }

}
