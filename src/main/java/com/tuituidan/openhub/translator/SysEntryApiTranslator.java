package com.tuituidan.openhub.translator;

import com.tuituidan.openhub.bean.vo.SysEntryApiView;
import com.tuituidan.openhub.service.CacheService;
import com.tuituidan.tresdin.datatranslate.bean.TranslationParameter;
import com.tuituidan.tresdin.datatranslate.translator.ITranslator;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * SysAppTranslator.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/7
 */
@Component
@Slf4j
public class SysEntryApiTranslator implements ITranslator<SysEntryApiAnno> {

    @Resource
    private CacheService cacheService;

    @Override
    public String getFieldName(String fieldName) {
        return "entryApiName";
    }

    @Override
    public String translate(TranslationParameter translationParameter) {
        Long value = (Long) translationParameter.getFieldValue();
        if (Objects.isNull(value)) {
            return StringUtils.EMPTY;
        }
        return Optional.ofNullable(cacheService.getEntryApiViewCache().getIfPresent(value))
                .map(SysEntryApiView::getName).orElse(StringUtils.EMPTY);
    }

}
