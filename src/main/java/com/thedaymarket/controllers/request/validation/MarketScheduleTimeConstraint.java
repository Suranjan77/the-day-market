package com.thedaymarket.controllers.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MarketScheduleTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MarketScheduleTimeConstraint {
  String message() default "Invalid time range, It should be between 10 AM and 5 PM";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
