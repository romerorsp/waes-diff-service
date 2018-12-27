package com.wearewaes.diff.rs.validation;

import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

  public void initialize(ValidUUID constraint) {
  }

  public boolean isValid(String uuid, ConstraintValidatorContext context) {
    try {
      UUID.fromString(uuid);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
