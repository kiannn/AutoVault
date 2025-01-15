
package com.example.CarDealerShip.ConstraintValidators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordPatternValidator.class) 
@Target( ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordPattern {
    
    String message() default "Passwords must have at least 8 characters and contain at least two of the following: uppercase letters, lowercase letters, numbers, and symbols from !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
