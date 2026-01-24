package com.example.CarDealerShip.Models;

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
import jakarta.persistence.TableGenerator;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqName")
    @TableGenerator(name = "seqName", allocationSize = 3, initialValue = 10)
    /**
     * If you’re working with a MySQL database, you should always use
     * GenerationType.IDENTITY. It uses an auto-incremented database column and
     * is the most efficient approach available. You can do that by annotating
     * your primary key attribute with @GeneratedValue(strategy =
     * GenerationType.IDENTITY). You can’t use SEQUENCE strategy with a MySQL
     * database, it requires a database sequence, and MySQL doesn’t support this
     * feature.
     */
    Integer itemNo;

    @Column(name = "available_make")
    String make;
 
    String model;

    @Column(name = "Generation")
    Integer year;

    LocalDate datePurchased;

    @Enumerated(EnumType.STRING)
    Transmissions powerTrain;

    @Column(name = "vehicle value")
    Double price;

    String condn;

    @Column(name = "H.P(watt)")
    Double horsePower;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    Owner owner;

    @OneToMany(mappedBy = "car", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)//, fetch = FetchType.EAGER) //  CascadeType.MERGE is needed when a Car record is edited so that newly added Documents are persisted
    List<Documents> docs;
}
