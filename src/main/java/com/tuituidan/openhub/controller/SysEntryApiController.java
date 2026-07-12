package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.dto.SysEntryApiDto;
import com.tuituidan.openhub.bean.entity.SysEntryApi;
import com.tuituidan.openhub.service.SysEntryApiService;
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
 * SysEntryApiController.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@RestController
@RequestMapping(TresdinConsts.API_V1 + "/entry_api")
public class SysEntryApiController {

    @Resource
    private SysEntryApiService sysEntryApiService;

    @GetMapping("/list")
    public List<SysEntryApi> selectList(SysEntryApi search) {
        return sysEntryApiService.selectList(search);
    }

    /**
     * add
     *
     * @param param param
     */
    @PostMapping
    public void add(@RequestBody SysEntryApiDto param) {
        sysEntryApiService.add(param);
    }

    /**
     * update
     *
     * @param id id
     * @param param param
     */
    @PatchMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody SysEntryApiDto param) {
        sysEntryApiService.update(id, param);
    }

    /**
     * delete
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysEntryApiService.delete(id);
    }

}
