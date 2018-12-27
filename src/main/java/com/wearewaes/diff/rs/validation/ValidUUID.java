package com.wearewaes.diff.rs.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.wearewaes.diff.rs.validation.ValidUUID.List;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)

@Repeatable(List.class)
@Constraint(validatedBy = UUIDValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface ValidUUID {

  String message() default "The ID must be a valid UUID.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
      ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
  public @interface List {

    ValidUUID[] value();
  }
}