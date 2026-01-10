package com.tuituidan.openhub.bean.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import com.tuituidan.openhub.config.mybatis.HttpAuthTypeHandler;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.tresdin.mybatis.bean.IEntity;
import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.ColumnType;

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
@Table(name = "sys_entry_api", schema = "msg_gateway")
public class SysEntryApi implements IEntity<SysEntryApi, Long> {

    private static final long serialVersionUID = -6110348488406941040L;

    @Id
    private Long id;

    private Long typeId;

    private String path;

    private String typeExp;

    private String name;

    @ColumnType(typeHandler = HttpAuthTypeHandler.class)
    private HttpAuthDto httpAuth;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime updateTime;

}
