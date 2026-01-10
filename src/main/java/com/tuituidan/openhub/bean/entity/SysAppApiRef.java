package com.tuituidan.openhub.bean.entity;

import java.io.Serializable;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysAppApiRef.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "sys_app_api_ref", schema = "msg_gateway")
public class SysAppApiRef implements Serializable {

    private static final long serialVersionUID = 1403552241504502986L;

    private Long appId;

    private Long entryApiId;

}
