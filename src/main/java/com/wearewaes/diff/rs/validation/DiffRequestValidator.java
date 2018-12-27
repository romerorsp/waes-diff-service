package com.wearewaes.diff.rs.validation;

import com.wearewaes.diff.rs.request.DiffRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

public class DiffRequestValidator implements ConstraintValidator<ValidDiffRequest, DiffRequest> {

  public void initialize(ValidDiffRequest constraint) {
  }

  public boolean isValid(DiffRequest request, ConstraintValidatorContext context) {
    final String value = request.getValue();

    if (StringUtils.isEmpty(value)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
          "Source value of the diff cant be empty.")
          .addBeanNode().addConstraintViolation();
      return false;
    } else if (!Base64.isBase64(value)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
          "Sources content needs to be base64 encoded.")
          .addBeanNode().addConstraintViolation();
      return false;
    }
    return true;
  }
}
