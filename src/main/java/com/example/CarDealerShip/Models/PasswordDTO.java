
package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.PasswordMatches;
import com.example.CarDealerShip.ConstraintValidators.PasswordPattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches 
@NoArgsConstructor 
/**
 * Why @NoArgsConstructor ?
 * When a password update is completed successfully,
 * the user is redirected back to the password update page. The 'passObj' model
 * attribute contains the data submitted through the form fields during the
 * password change, and this data should not be displayed upon returning to the
 * password update page. Therefore, a new PasswordDTO object must be created,
 * and the passObj model attribute should be associated with this newly created
 * object.(line 89 at AccountController.java:  mp.addAttribute("passObj", new PasswordDTO()) );
 */
public class PasswordDTO {
    
    String currentPass;
    
    @PasswordPattern
    String newPass;
    
    String confirmPass;
    
}
