package com.tuituidan.openhub.config.mybatis;

import com.alibaba.fastjson2.JSON;
import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * HttpAuthTypeHandler.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-08
 */
@Slf4j
public class HttpAuthTypeHandler extends BaseTypeHandler<HttpAuthDto> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, HttpAuthDto parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public HttpAuthDto getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JSON.parseObject(rs.getString(columnName), HttpAuthDto.class);
    }

    @Override
    public HttpAuthDto getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JSON.parseObject(rs.getString(columnIndex), HttpAuthDto.class);
    }

    @Override
    public HttpAuthDto getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JSON.parseObject(cs.getString(columnIndex), HttpAuthDto.class);
    }

}
