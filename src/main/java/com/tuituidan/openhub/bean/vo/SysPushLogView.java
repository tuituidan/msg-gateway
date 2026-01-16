package com.tuituidan.openhub.bean.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.translator.SysAppAnno;
import com.tuituidan.tresdin.datatranslate.translator.dict.DictType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * SysPushLogView.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/10
 */
@Getter
@Setter
public class SysPushLogView {

    private Long id;

    private Long logId;

    @SysAppAnno
    private Long appId;

    @DictType("2000000002")
    private String status;

    private String response;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pushTime;

    private Long costTime;

    private Integer pushTimes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
