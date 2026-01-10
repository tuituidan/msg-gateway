package com.tuituidan.openhub.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * EntryStatusEnum.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-08
 */
@Getter
@AllArgsConstructor
public enum EntryStatusEnum {
    /**
     * 成功
     */
    SUCCESS("01", "接收成功"),
    UNKNOWN("02", "没有匹配的api接收"),
    UN_AUTH("03", "接口认证未通过");

    private String code;

    private String name;
}
