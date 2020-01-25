package com.zpffly.crush.validator;

import com.zpffly.crush.annotation.IsMobile;
import com.zpffly.crush.util.ValidatorUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = true;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!required && StringUtils.isEmpty(s))
            return true;
        return ValidatorUtil.isMobile(s);
    }
}
