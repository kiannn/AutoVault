package com.example.CarDealerShip.ConstraintValidators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConstraint {

    String message() default "Invalid date: exceeds maximum limit of 100 years from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};  

}
