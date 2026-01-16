CREATE TABLE IF NOT EXISTS `sys_app` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `app_key` varchar(100) NOT NULL COMMENT '应用标识',
    `app_secret` varchar(100) NOT NULL COMMENT '应用秘钥',
    `app_name` varchar(200) NOT NULL COMMENT '应用名称',
    `url` varchar(200) DEFAULT NULL COMMENT '地址',
    `result_exp` varchar(400) DEFAULT NULL COMMENT '结果解析表达式',
    `http_auth` varchar(1000) DEFAULT NULL COMMENT '请求验证',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='应用';

CREATE TABLE IF NOT EXISTS `sys_entry_api_type` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `name` varchar(100) NOT NULL COMMENT '类型名称',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='接口类型配置';

CREATE TABLE IF NOT EXISTS `sys_entry_api` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `type_id` bigint(20) NOT NULL COMMENT '类型ID',
    `path` varchar(100) NOT NULL COMMENT '拦截地址',
    `type_exp` varchar(100) DEFAULT NULL COMMENT '数据类型表达式',
    `name` varchar(100) NOT NULL COMMENT '接口名称',
    `http_auth` varchar(1000) DEFAULT NULL COMMENT '请求验证',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='接口配置';

CREATE TABLE IF NOT EXISTS `sys_app_api_ref` (
    `app_id` bigint(20) NOT NULL COMMENT '应用ID',
    `entry_api_id` bigint(20) NOT NULL COMMENT '接口ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='应用接口关联';

CREATE TABLE IF NOT EXISTS `sys_entry_api_log` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `entry_api_id` bigint(20) DEFAULT NULL COMMENT '接口ID',
    `path` varchar(100) NOT NULL COMMENT '数据库名',
    `client_ip` varchar(100) DEFAULT NULL COMMENT '表名',
    `headers` varchar(4000) DEFAULT NULL COMMENT '请求头',
    `body` varchar(8000) DEFAULT NULL COMMENT '请求体',
    `status` varchar(100) DEFAULT NULL COMMENT '状态',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='接口日志';


CREATE TABLE IF NOT EXISTS `sys_push_log` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `log_id` bigint(20) NOT NULL COMMENT '数据日志ID',
    `app_id` bigint(20) NOT NULL COMMENT '应用ID',
    `status` varchar(100) NOT NULL COMMENT '推送状态',
    `response` varchar(4000) NOT NULL COMMENT '推送结果',
    `push_time` datetime NOT NULL COMMENT '推送时间',
    `cost_time` bigint(20) NOT NULL COMMENT '推送耗时',
    `push_times` int(11) NOT NULL COMMENT '推送次数',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='数据推送日志';
