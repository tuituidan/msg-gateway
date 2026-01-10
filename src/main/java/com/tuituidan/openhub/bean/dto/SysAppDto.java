package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysAppDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-09
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysAppDto {
    private String appKey;

    private String appSecret;

    private String appName;

    private String url;

    private HttpAuthDto httpAuth;

    private String resultExp;
}
