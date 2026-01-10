package com.tuituidan.openhub.bean.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.tresdin.mybatis.bean.IEntity;
import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysEntryApi.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "sys_entry_api_type", schema = "msg_gateway")
public class SysEntryApiType implements IEntity<SysEntryApiType, Long> {

    private static final long serialVersionUID = 4808385970426033633L;

    @Id
    private Long id;

    private String name;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime updateTime;

}
