package com.tuituidan.openhub.bean.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysEntryApiDto.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysEntryApiDto {

    private String path;

    private String typeExp;

    private String name;

    private HttpAuthDto httpAuth;

}
