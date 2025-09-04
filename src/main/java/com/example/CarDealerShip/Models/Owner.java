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
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Owner {

    String firstName;

    String lastName;

    String email;

    LocalDate dob;

    @Id
    String username;

    @Embedded
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
