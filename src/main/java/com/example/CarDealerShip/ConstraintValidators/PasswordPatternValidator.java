
package com.example.CarDealerShip.ConstraintValidators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import static java.util.regex.Pattern.matches;

public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("Pattern isValid()  ==>"+value);
        if(value==null){return true;} 
     /**
      * if(value==null){return true;} 
      * happens when delete account - in which case only two fields of PasswordDTO -'currentPass' and 'confirmPass' -
      * are needed and present in deleteAccountPage page's form, however @PasswordPattern is still applied on the PasswordDTO's third filed 
      * 'newPass' which is not present (and not needed) in the deleteAccountPage page's form.
      * we make use of 'newPass' in case update password is performed in which the new password entered by user must have a valid pattern.
      */ 
        boolean matches1 = matches("[\\d]{8,}", value);
        boolean matches2 = matches("[a-z]{8,}", value);
        boolean matches3 = matches("[A-Z]{8,}", value);  
        boolean matches4 = matches("[!\"#$%&'()*+,\\-./:;<=>?@[\\\\]^_`{|}~]{8,}", value); 
        boolean matches5 = !matches("[\\w!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~]{8,}", value);

        boolean  b = !(matches1 || matches2 || matches3 || matches4 || matches5) ;
        
        return b;
    }
    
}
