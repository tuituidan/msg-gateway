package com.tuituidan.openhub.util;

import com.tuituidan.openhub.bean.dto.HttpAuthDto;
import com.tuituidan.openhub.bean.dto.KeyValueDto;
import com.tuituidan.openhub.consts.AuthTypeEnum;
import com.tuituidan.openhub.consts.Consts;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

/**
 * HttpAuthUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-07
 */
@UtilityClass
public class HttpAuthUtils {

    public static void checkHttpAuth(Map<String, String> headers, HttpAuthDto authDto) {
        if (AuthTypeEnum.KEY_VALUE.getType().equals(authDto.getAuthType())) {
            for (KeyValueDto keyValue : authDto.getAuthKeyValues()) {
                Assert.isTrue(Objects.equals(keyValue.getValue(), headers.get(keyValue.getKey().toLowerCase())),
                        "header头验证失败");
            }
        }
        if (AuthTypeEnum.BASIC.getType().equals(authDto.getAuthType())) {
            Assert.isTrue(Objects.equals(headers.get(HttpHeaders.AUTHORIZATION.toLowerCase()),
                    Consts.HTTP_HEADER_BASIC_PREFIX
                            + HttpHeaders.encodeBasicAuth(authDto.getBasicUsername(),
                            authDto.getBasicPassword(), StandardCharsets.UTF_8)), "Basic认证的失败");
        }
        if (AuthTypeEnum.BEARER.getType().equals(authDto.getAuthType())) {
            Assert.isTrue(Objects.equals(headers.get(HttpHeaders.AUTHORIZATION.toLowerCase()),
                    Consts.HTTP_HEADER_BEARER_PREFIX + authDto.getBearerToken()), "Bearer认证的失败");
        }
        if (AuthTypeEnum.JWT.getType().equals(authDto.getAuthType())) {
            try {
                Assert.notNull(Jwts.parser().setSigningKey(authDto.getJwtSecret())
                        .parse(headers.get(HttpHeaders.AUTHORIZATION.toLowerCase())).getBody(), "jwt认证失败");
            } catch (Exception ex) {
                throw new IllegalArgumentException("jwt认证失败");
            }
        }
    }

    public static void checkAppHttpAuth(HttpAuthDto authDto) {
        if (AuthTypeEnum.KEY_VALUE.getType().equals(authDto.getAuthType())) {
            List<KeyValueDto> list = authDto.getAuthKeyValues().stream()
                    .filter(item -> StringUtils.isNoneBlank(item.getKey(), item.getValue()))
                    .collect(Collectors.toList());
            Assert.notEmpty(list, "认证键值对不能为空");
            authDto.setAuthKeyValues(list);
        }
        if (AuthTypeEnum.BASIC.getType().equals(authDto.getAuthType())) {
            Assert.isTrue(StringUtils.isNoneBlank(authDto.getBasicUsername(),
                    authDto.getBasicPassword()), "Basic认证的账号密码不能为空");
        }
        if (AuthTypeEnum.BEARER.getType().equals(authDto.getAuthType())) {
            Assert.hasText(authDto.getBearerToken(), "Bearer Token认证的token不能为空");
            if (!StringUtils.startsWith(authDto.getBearerToken(), Consts.HTTP_HEADER_BEARER_PREFIX)) {
                authDto.setBearerToken(Consts.HTTP_HEADER_BEARER_PREFIX + authDto.getBearerToken());
            }
        }
        if (AuthTypeEnum.JWT.getType().equals(authDto.getAuthType())) {
            Assert.hasText(authDto.getJwtSecret(), "jwt认证的密钥不能为空");
            List<KeyValueDto> list = authDto.getJwtPayload().stream()
                    .filter(item -> StringUtils.isNoneBlank(item.getKey(), item.getValue()))
                    .collect(Collectors.toList());
            authDto.setJwtPayload(list);
        }
    }

    public static HttpHeaders buildHttpHeaders(HttpAuthDto authDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (authDto == null || StringUtils.isBlank(authDto.getAuthType())) {
            return httpHeaders;
        }
        if (AuthTypeEnum.KEY_VALUE.getType().equals(authDto.getAuthType())) {
            for (KeyValueDto item : authDto.getAuthKeyValues()) {
                if (StringUtils.isNoneBlank(item.getKey(), item.getValue())) {
                    httpHeaders.add(item.getKey(), item.getValue());
                }
            }
            return httpHeaders;
        }
        if (AuthTypeEnum.BASIC.getType().equals(authDto.getAuthType())) {
            httpHeaders.setBasicAuth(authDto.getBasicUsername(), authDto.getBasicPassword(),
                    StandardCharsets.UTF_8);
            return httpHeaders;
        }
        if (AuthTypeEnum.BEARER.getType().equals(authDto.getAuthType())) {
            httpHeaders.setBearerAuth(authDto.getBearerToken());
            return httpHeaders;
        }
        if (AuthTypeEnum.JWT.getType().equals(authDto.getAuthType())) {
            List<KeyValueDto> list = authDto.getJwtPayload().stream()
                    .filter(it -> StringUtils.isNoneBlank(it.getKey(), it.getValue())).collect(Collectors.toList());
            Map<String, Object> map = list.stream().collect(Collectors.toMap(KeyValueDto::getKey,
                    KeyValueDto::getValue));
            SignatureAlgorithm algorithm = SignatureAlgorithm.forName(authDto.getJwtAlgorithm());
            httpHeaders.setBearerAuth(Jwts.builder().setClaims(map).signWith(algorithm,
                    authDto.getJwtSecret()).compact());
            return httpHeaders;
        }
        return httpHeaders;
    }

}
