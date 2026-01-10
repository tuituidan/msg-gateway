package com.tuituidan.openhub.bean.dto;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * HttpRequestDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
public class HttpRequestDto {

    private String url;

    private String clientIp;

    private Map<String, String> headers;

    private Object body;

}
