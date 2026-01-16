package com.tuituidan.openhub.bean.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import com.tuituidan.openhub.consts.Consts;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysAppView.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-15
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysAppView implements Serializable {

    private static final long serialVersionUID = 1508775689927540905L;

    private Long id;

    private String appKey;

    private String appSecret;

    private String appName;

    private String url;

    private HttpAuthDto httpAuth;

    private String resultExp;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime updateTime;

}
