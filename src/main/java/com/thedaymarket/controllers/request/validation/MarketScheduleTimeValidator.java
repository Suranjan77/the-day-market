package com.thedaymarket.controllers.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class MarketScheduleTimeValidator
    implements ConstraintValidator<MarketScheduleTimeConstraint, LocalTime> {
  @Override
  public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
    if (value == null) return false;

    LocalTime start = LocalTime.of(10, 0);
    LocalTime end = LocalTime.of(17, 0);

    return !value.isBefore(start) && !value.isAfter(end);
  }
}
