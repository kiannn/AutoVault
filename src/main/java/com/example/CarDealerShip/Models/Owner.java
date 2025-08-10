package com.example.CarDealerShip.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Owner {

    @NotBlank(message = "First name can not be empty")
    String firstName;

    @NotBlank(message = "Last name can not be empty")
    String lastName;

    @Email(message = "Enter the email address in the format someone@example.com.")
    @NotNull
    String email;

    @Past(message = "Invalid date of bith, can not be in future.")
    LocalDate dob;

    @Id
    @NotBlank(message = "Username can not be empty")
    @Pattern(regexp = "[^\\s]+.*", message = "Username can not start with spaces")
    String username;

    @Embedded
    @Valid
    Credentials credentials;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "availableMakeAndModels", joinColumns = @JoinColumn(name = "Owner username", referencedColumnName = "username"))
    List<MakeAndModel> MakeAndModel;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<Car> cars;

    public boolean isAccountNonExpired() {

        return true;
    }

    public boolean isAccountNonLocked() {

        return true;
    }

    public boolean isCredentialsNonExpired() {

        return true;
    }

    public boolean isEnabled() {

        return true;
    }
}
