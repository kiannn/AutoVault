
package com.example.CarDealerShip.ConstraintValidators;

import com.example.CarDealerShip.ConstraintValidators.PasswordMatches;
import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.Credentials;
import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Models.PasswordDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {// Credentials> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
   
        boolean cred = obj instanceof Credentials;
        boolean dto = obj instanceof PasswordDTO;

        
        if (cred) {
            return ((Credentials) obj).getPassword() != null && ((Credentials) obj).getPassword().equals(((Credentials) obj).getConfirmPassword());
        }
        if (dto) {
            PasswordDTO passwordDTO = (PasswordDTO) obj;
            if (passwordDTO.getNewPass() != null) {
                return passwordDTO.getNewPass().equals(passwordDTO.getConfirmNewPass());
            }
            if (passwordDTO.getCurrentPass() != null) {
                return ((PasswordDTO) obj).getCurrentPass().equals(((PasswordDTO) obj).getConfirmNewPass());
            }
            return false;
        } else {
            return ((Owner) obj).getCredentials() != null && ((Owner) obj).getCredentials().getPassword() != null && ((Owner) obj).getCredentials().getPassword().equals(((Owner) obj).getCredentials().getConfirmPassword());

        }

    }
}
