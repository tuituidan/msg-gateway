package com.tuituidan.openhub.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.translator.SysEntryApiAnno;
import com.tuituidan.tresdin.datatranslate.translator.dict.DictType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * SysEntryApiLogView.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/10
 */
@Getter
@Setter
public class SysEntryApiLogView {
    private Long id;

    private String path;

    private String clientIp;

    private String headers;

    private String body;

    @SysEntryApiAnno
    private Long entryApiId;

    @DictType("2000000001")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
