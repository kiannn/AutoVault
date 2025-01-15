
package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.PasswordMatches;
import com.example.CarDealerShip.ConstraintValidators.PasswordPattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches 
public class PasswordDTO {
    
    String currentPass;
    
    @PasswordPattern
    String newPass;
    
    String confirmNewPass;
    
}
