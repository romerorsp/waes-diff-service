package com.wearewaes.diff.rs.validation;

import com.wearewaes.diff.rs.exception.DiffException;
import com.wearewaes.diff.rs.exception.TransformationException;
import com.wearewaes.diff.rs.response.DiffError;
import com.wearewaes.diff.rs.response.DiffResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidDiffRequestControllerAdvice {

  private MessageSource messageSource;

  @Autowired
  public ValidDiffRequestControllerAdvice(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ResponseBody
  @ExceptionHandler(TransformationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public DiffResponse<Void> processTransformationException(TransformationException ex) {
    return DiffResponse.<Void>builder()
        .success(false)
        .errors(Collections.singletonList(new DiffError(ex.getMessage())))
        .build();
  }

  @ResponseBody
  @ExceptionHandler(DiffException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public DiffResponse<Void> processDiffException(DiffException ex) {
    return DiffResponse.<Void>builder()
        .success(false)
        .errors(Collections.singletonList(new DiffError(ex.getMessage())))
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public DiffResponse<Void> processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<ObjectError> allErrors = result.getAllErrors();

    return parseFieldErrors(allErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public DiffResponse<Void> processConstraintValidationException(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

    return parseViolations(violations);
  }

  private DiffResponse<Void> parseViolations(Set<ConstraintViolation<?>> violations) {
    return DiffResponse.<Void>builder()
        .success(false)
        .errors(
            violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(DiffError::new)
                .collect(Collectors.toList()))
        .build();
  }

  private DiffResponse<Void> parseFieldErrors(List<ObjectError> objectErrors) {
    final List<DiffError> errors = objectErrors.stream()
        .map(this::findLocalizedErrorMessage)
        .map(DiffError::new)
        .collect(Collectors.toList());
    return DiffResponse.<Void>builder()
        .success(false)
        .errors(errors)
        .build();
  }

  private String findLocalizedErrorMessage(ObjectError objectError) {
    Locale currentLocale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(objectError, currentLocale);
  }
}