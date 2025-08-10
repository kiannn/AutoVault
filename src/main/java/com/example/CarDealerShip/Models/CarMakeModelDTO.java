
package com.example.CarDealerShip.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarMakeModelDTO {
        
    @JsonProperty("Make_Name")
    String make;
    
    @JsonProperty("Model_Name")
    @Pattern(regexp = "[\\w\\s/.-]*", message = "invalid value for model, only letters and numbers are accepted")
    String model;
    
}
