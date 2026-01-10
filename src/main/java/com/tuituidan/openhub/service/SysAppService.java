package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.SysAppDto;
import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.bean.entity.SysAppApiRef;
import com.tuituidan.openhub.mapper.SysAppApiRefMapper;
import com.tuituidan.openhub.mapper.SysAppMapper;
import com.tuituidan.openhub.util.HttpAuthUtils;
import com.tuituidan.tresdin.util.BeanExtUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * SysAppService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Service
public class SysAppService {

    @Resource
    private SysAppMapper sysAppMapper;

    @Resource
    private SysAppApiRefMapper sysAppApiRefMapper;

    /**
     * selectApiIdsByAppId
     *
     * @param appId appId
     * @return List
     */
    public List<Long> selectApiIdsByAppId(Long appId) {
        return sysAppApiRefMapper.select(new SysAppApiRef().setAppId(appId)).stream()
                .map(SysAppApiRef::getEntryApiId).collect(Collectors.toList());
    }

    public void saveAppApi(Long appId, Long[] apiIds) {
        sysAppApiRefMapper.delete(new SysAppApiRef().setAppId(appId));
        if (ArrayUtils.isNotEmpty(apiIds)) {
            sysAppApiRefMapper.insertList(Arrays.stream(apiIds)
                    .map(apiId -> new SysAppApiRef().setAppId(appId)
                            .setEntryApiId(apiId)).collect(Collectors.toList()));
        }
    }

    /**
     * selectAll
     *
     * @return List
     */
    public List<SysApp> selectAll() {
        return sysAppMapper.selectAll();
    }

    /**
     * save
     *
     * @param id id
     * @param param param
     */
    public void save(Long id, SysAppDto param) {
        checkUnique(id, param);
        HttpAuthUtils.checkAppHttpAuth(param.getHttpAuth());
        SysApp saveItem = BeanExtUtils.convert(param, SysApp::new);
        if (id == null) {
            sysAppMapper.insertSelective(saveItem);
            return;
        }
        saveItem.setId(id);
        sysAppMapper.updateByPrimaryKeySelective(saveItem);
    }

    private void checkUnique(Long id, SysAppDto param) {
        SysApp exist = sysAppMapper.selectOne(new SysApp().setAppKey(param.getAppKey()));
        Assert.isTrue(Objects.isNull(exist) || Objects.equals(id, exist.getId()),
                "已存在相同应用标识的应用");
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(Long id) {
        sysAppMapper.deleteByPrimaryKey(id);
    }

}
