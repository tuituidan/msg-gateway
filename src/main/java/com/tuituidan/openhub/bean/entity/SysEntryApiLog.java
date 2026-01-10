package com.tuituidan.openhub.bean.entity;

import com.tuituidan.tresdin.mybatis.bean.IEntity;
import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysEntryApiLog.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-08
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "sys_entry_api_log", schema = "msg_gateway")
public class SysEntryApiLog implements IEntity<SysEntryApiLog, Long> {

    private static final long serialVersionUID = -6069242239097967216L;

    @Id
    private Long id;

    private String path;

    private String clientIp;

    private String headers;

    private String body;

    private Long entryApiId;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
