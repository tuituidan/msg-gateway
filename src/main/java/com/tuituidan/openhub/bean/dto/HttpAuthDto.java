package com.tuituidan.openhub.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * HttpApiBuilder.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
public class HttpAuthDto implements Serializable {

    private static final long serialVersionUID = -1620396699529677427L;

    private String authType;

    private List<KeyValueDto> authKeyValues;

    private String basicUsername;

    private String basicPassword;

    private String bearerToken;

    private String jwtAlgorithm;

    private String jwtSecret;

    private List<KeyValueDto> jwtPayload;

}
