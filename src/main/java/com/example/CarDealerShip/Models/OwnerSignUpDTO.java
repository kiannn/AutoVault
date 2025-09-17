
package com.example.CarDealerShip.Models;

import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
public class OwnerSignUpDTO {
    
    @NotBlank(message = "First name can not be empty")
    String firstName;

    @NotBlank(message = "Last name can not be empty")
    String lastName;

    @Email(message = "Enter the email address in the format someone@example.com.")
//    @NotNull // This annotation is not required here, because if the user leaves 'email' empty, its value will be set to an empty string, not null.
    String email;

    @DateTimeFormat(pattern = "yyyy-MMM-dd", fallbackPatterns ={"yyyy-MM-dd","dd/MMM/yyyy","dd.MM.yyyy","MM/dd/yyyy","dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM"})
    @Past(message = "Invalid date of bith, can not be in future.")
    LocalDate dob;

    @NotBlank(message = "Username can not be empty")
    @Pattern(regexp = "[^\\s]+.*", message = "Username can not start with spaces")
    String username;

    @Embedded
    @Valid
    Credentials credentials;
}
