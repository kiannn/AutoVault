package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.DateConstraint;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter // CarWithDocsDTO is used as a parameter in the controller method when binding form data to the controller. It must have setters to allow values from the form to be assigned to its properties.
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarWithDocsDTO {

    Integer itemNo;
    String make;
    
    String model;
    Integer year;
    
    @DateConstraint
    @Past
    LocalDate datePurchased;
    
    Transmissions powerTrain;
    
    @Positive(message = "invalid price, should be a positive value")
    Double price;
    String condn;
    
    @PositiveOrZero(message = "invalid hp, should be a non-negative value")
    Double horsePower;
    Integer docId;
    String docName;
    Long docSize;

    
    

}
