package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import com.tuituidan.openhub.bean.dto.SysEntryApiDto;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.bean.entity.SysEntryApiType;
import com.tuituidan.openhub.bean.vo.TreeView;
import com.tuituidan.openhub.consts.AuthTypeEnum;
import com.tuituidan.openhub.mapper.SysEntryApiMapper;
import com.tuituidan.openhub.mapper.SysEntryApiTypeMapper;
import com.tuituidan.tresdin.mybatis.util.SnowFlake;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
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
    private SysEntryApiTypeMapper sysEntryApiTypeMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //tempInit();
        List<SysEntryApi> entryApis = sysEntryApiMapper.selectAll();
        for (SysEntryApi entryApi : entryApis) {
            registerMapping(entryApi);
        }
    }

    private void tempInit() {
        SysEntryApiType apiType = new SysEntryApiType().setId(SnowFlake.newId()).setName("主数据接口");
        sysEntryApiTypeMapper.insert(apiType);
        Map<String, String> apiMap = new HashMap<>();
        apiMap.put("cussup", "客商主数据接口");
        apiMap.put("dept", "部门主数据接口");
        apiMap.put("company", "机构主数据接口");
        apiMap.put("emp", "人员主数据接口");
        apiMap.put("project", "项目主数据接口");
        apiMap.put("tecsub", "科研课题主数据接口");
        apiMap.put("user", "用户主数据接口");
        HttpAuthDto httpAuth = new HttpAuthDto()
                .setAuthType(AuthTypeEnum.BASIC.getType())
                .setBasicUsername("admin")
                .setBasicPassword("Admin2024xbjsQAQ");
        for (Entry<String, String> entry : apiMap.entrySet()) {
            SysEntryApi entryApi = new SysEntryApi()
                    .setId(SnowFlake.newId())
                    .setName(entry.getValue())
                    .setPath("/mid-data/receive/data")
                    .setTypeId(apiType.getId())
                    .setHttpAuth(httpAuth)
                    .setTypeExp("#dataType == '" + entry.getKey() + "'");
            sysEntryApiMapper.insertSelective(entryApi);
        }
        SysEntryApi entryApi = new SysEntryApi()
                .setId(SnowFlake.newId())
                .setName("公共基础主数据接口")
                .setPath("/mid-data/receive/data")
                .setTypeId(apiType.getId())
                .setHttpAuth(httpAuth);
        sysEntryApiMapper.insertSelective(entryApi);
    }

    public List<TreeView> entryApiTree() {
        List<SysEntryApiType> typeList = sysEntryApiTypeMapper.selectAll();
        List<SysEntryApi> apiList = sysEntryApiMapper.selectAll();
        Map<Long, List<SysEntryApi>> apiMap = apiList.stream().collect(Collectors.groupingBy(SysEntryApi::getTypeId));
        List<TreeView> result = new ArrayList<>();
        for (SysEntryApiType type : typeList) {
            List<SysEntryApi> children = apiMap.get(type.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                TreeView view = new TreeView();
                view.setId(type.getId().toString());
                view.setName(type.getName());
                view.setChildren(children.stream()
                        .map(it -> new TreeView()
                                .setId(it.getId().toString())
                                .setName(it.getName())
                                .setPid(type.getId().toString()))
                        .collect(Collectors.toList()));
                result.add(view);
            }
        }
        return result;
    }

    public void add(SysEntryApiDto entryApiDto) {
        checkRepeat(null, entryApiDto);
        SysEntryApi entryApi = BeanExtUtils.convert(entryApiDto, SysEntryApi::new);
        entryApi.setId(SnowFlake.newId());
        registerMapping(entryApi);
        sysEntryApiMapper.insert(entryApi);
    }

    public void update(Long id, SysEntryApiDto entryApiDto) {
        checkRepeat(id, entryApiDto);
        SysEntryApi oldEntryApi = sysEntryApiMapper.selectByPrimaryKey(id);
        if (Objects.equals(oldEntryApi.getPath(), entryApiDto.getPath())) {
            BeanExtUtils.copyProperties(entryApiDto, oldEntryApi);
            sysEntryApiMapper.updateByPrimaryKeySelective(oldEntryApi);
            return;
        }
        // path不相同也应该判断是否有关联业务数据？
        requestMappingHandlerMapping.unregisterMapping(buildRequestMappingInfo(oldEntryApi));
        BeanExtUtils.copyProperties(entryApiDto, oldEntryApi);
        requestMappingHandlerMapping.registerMapping(buildRequestMappingInfo(oldEntryApi),
                msgGatewayService, msgGatewayService.getMethod());
        sysEntryApiMapper.updateByPrimaryKeySelective(oldEntryApi);
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

    public void remove(Long id) {
        SysEntryApi entryApi = sysEntryApiMapper.selectByPrimaryKey(id);
        // 需要判断有没有关联的业务数据，否则不允许删除
        requestMappingHandlerMapping.unregisterMapping(buildRequestMappingInfo(entryApi));
    }

    private RequestMappingInfo buildRequestMappingInfo(SysEntryApi entryApi) {
        return RequestMappingInfo
                .paths(entryApi.getPath()).methods(RequestMethod.POST)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .options(requestMappingHandlerMapping.getBuilderConfiguration())
                .build();
    }

}
