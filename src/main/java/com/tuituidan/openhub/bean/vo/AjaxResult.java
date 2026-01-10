package com.tuituidan.openhub.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * AjaxResult.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-08
 */
@Getter
@Setter
@Accessors(chain = true)
public class AjaxResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<T>().setCode(0).setMsg("操作成功");
    }

    public static <T> AjaxResult<T> fail(String msg) {
        return new AjaxResult<T>().setCode(1).setMsg(msg);
    }
}
