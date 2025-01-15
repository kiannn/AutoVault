package com.example.CarDealerShip.ConstraintValidators;

import com.example.CarDealerShip.ConstraintValidators.DateConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<DateConstraint, LocalDate> { 

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        return value == null || value.isBefore(LocalDate.now().plusYears(1000));
                                
    }

}
