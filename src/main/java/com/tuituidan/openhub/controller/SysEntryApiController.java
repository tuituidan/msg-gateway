package com.tuituidan.openhub.controller;

import com.tuituidan.openhub.bean.vo.TreeView;
import com.tuituidan.openhub.service.SysEntryApiService;
import com.tuituidan.tresdin.consts.TresdinConsts;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/tree")
    public List<TreeView> entryApiTree() {
        return sysEntryApiService.entryApiTree();
    }

}
