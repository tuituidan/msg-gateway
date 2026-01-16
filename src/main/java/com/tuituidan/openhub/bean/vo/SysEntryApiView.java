package com.tuituidan.openhub.bean.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import com.tuituidan.openhub.consts.Consts;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SysEntryApiView.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2026-01-15
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysEntryApiView implements Serializable {

    private static final long serialVersionUID = 7859210699306193731L;

    private Long id;

    private Long typeId;

    private String path;

    private String typeExp;

    private String name;

    private HttpAuthDto httpAuth;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Consts.TIME_PATTERN)
    @JSONField(format = Consts.TIME_PATTERN)
    private LocalDateTime updateTime;

    private List<SysAppView> appList;

}
