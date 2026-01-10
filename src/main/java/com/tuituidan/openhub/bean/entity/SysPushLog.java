package com.tuituidan.openhub.bean.entity;

import com.tuituidan.tresdin.mybatis.bean.IEntity;
import java.time.LocalDateTime;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysPushLog.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "sys_push_log", schema = "msg_gateway")
public class SysPushLog implements IEntity<SysPushLog, Long> {

    private static final long serialVersionUID = 8201212624114687524L;

    @Id
    private Long id;

    private Long logId;

    private Long appId;

    private String status;

    private String response;

    private LocalDateTime pushTime;

    private Long costTime;

    private Integer pushTimes;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
