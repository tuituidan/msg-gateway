package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.SysAppDto;
import com.tuituidan.openhub.bean.entity.SysApp;
import com.tuituidan.openhub.service.SysAppService;
import com.tuituidan.tresdin.consts.TresdinConsts;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SysAppController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-09
 */
@RestController
@RequestMapping(TresdinConsts.API_V1 + "/sys_app")
public class SysAppController {

    @Resource
    private SysAppService sysAppService;

    /**
     * selectApiIdsByAppId
     *
     * @param appId appId
     * @return List
     */
    @GetMapping("/{appId}/api")
    public List<Long> selectApiIdsByAppId(@PathVariable Long appId) {
        return sysAppService.selectApiIdsByAppId(appId);
    }

    /**
     * saveAppApi
     */
    @PostMapping("/{appId}/api")
    public void saveAppApi(@PathVariable Long appId, @RequestBody Long[] ids) {
        sysAppService.saveAppApi(appId, ids);
    }

    /**
     * selectAll
     *
     * @return List
     */
    @GetMapping
    public List<SysApp> selectAll() {
        return sysAppService.selectAll();
    }

    /**
     * add
     *
     * @param param param
     */
    @PostMapping
    public void add(@RequestBody SysAppDto param) {
        sysAppService.save(null, param);
    }

    /**
     * update
     *
     * @param id id
     * @param param param
     */
    @PatchMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody SysAppDto param) {
        sysAppService.save(id, param);
    }

    /**
     * delete
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysAppService.delete(id);
    }

}
