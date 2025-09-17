package com.example.CarDealerShip.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class OwnerStatDTO {

    @NotBlank(message = "First name can not be empty")
    String firstName;

    @NotBlank(message = "Last name can not be empty")
    String lastName;

    @DateTimeFormat(pattern = "yyyy-MMM-dd", fallbackPatterns ={"yyyy-MM-dd","dd/MMM/yyyy","dd.MM.yyyy","MM/dd/yyyy","dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM"})
    @Past(message = "Invalid date of bith, can not be in future")
    LocalDate dob;

    @Email(message = "Enter the email address in the format someone@example.com")
    String email;

    public OwnerStatDTO(String firstName, String lastName, LocalDate dob, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;

    }
}
