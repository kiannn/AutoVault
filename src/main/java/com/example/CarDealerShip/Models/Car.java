package com.example.CarDealerShip.Models;

import com.example.CarDealerShip.ConstraintValidators.DateConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "availableCars")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqName")
    @SequenceGenerator(name = "seqName", allocationSize = 3, initialValue = 10)
    Integer itemNo;

    @Column(name = "vehivle_make")
    @JsonProperty("Make_Name")
    String make;

    @Pattern(regexp = "[\\w\\s/.-]*", message = "invalid value for model, only letters and numbers are accepted")
    @JsonProperty("Model_Name")
    String model;

    @Column(name = "Generation")
    Integer year;

    @DateConstraint
    @Past
    LocalDate datePurchased;

    @Enumerated(EnumType.STRING)
    Transmissions powerTrain;

    @Positive(message = "invalid price, should be a positive value")
    @Column(name = "vehicle value")
    Double price;

    String condn;

    @Column(name = "H.P(watt)")
    @PositiveOrZero(message = "invalid hp, should be a non-negative value")
    Double horsePower;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    Owner owner;

    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)//, fetch = FetchType.EAGER) //  CascadeType.MERGE is needed when a Car record is edited so that newly added Documents are persisted   
    List<Documents> docs;

}
