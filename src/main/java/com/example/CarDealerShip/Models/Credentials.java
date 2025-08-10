package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.PasswordMatches;
import com.example.CarDealerShip.ConstraintValidators.PasswordPattern;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@PasswordMatches
@Embeddable
public class Credentials {

    @NotBlank(message = "Password can not be empty") //The annotated element must not be null and must contain at least one non-whitespace character
    @PasswordPattern
    String password;

    String confirmPassword;

    boolean enabled;

    @OneToMany(mappedBy = "carOwner", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    List<Authorities> authorities;

}
