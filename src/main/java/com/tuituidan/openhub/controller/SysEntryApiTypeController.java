package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.entity.SysEntryApiType;
import com.tuituidan.openhub.service.SysEntryApiTypeService;
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
@RequestMapping(TresdinConsts.API_V1 + "/entry_api_type")
public class SysEntryApiTypeController {

    @Resource
    private SysEntryApiTypeService sysEntryApiTypeService;

    @GetMapping("/list")
    public List<SysEntryApiType> selectList() {
        return sysEntryApiTypeService.selectList();
    }

    /**
     * add
     *
     * @param param param
     */
    @PostMapping
    public void add(@RequestBody SysEntryApiType param) {
        sysEntryApiTypeService.save(null, param.getName());
    }

    /**
     * update
     *
     * @param id id
     * @param param param
     */
    @PatchMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody SysEntryApiType param) {
        sysEntryApiTypeService.save(id, param.getName());
    }

    /**
     * delete
     *
     * @param id id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysEntryApiTypeService.delete(id);
    }

}
