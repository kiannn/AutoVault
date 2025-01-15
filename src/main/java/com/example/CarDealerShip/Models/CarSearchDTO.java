package com.example.CarDealerShip.Models;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class CarSearchDTO {

    String make;
    
    List<String> modelList;

    Integer year;
    Integer yearTo;

    LocalDate datePurchased ;
    LocalDate datePurchasedTo;

    Double price;
    Double priceTo;

    public CarSearchDTO(String make, List<String> modelList, Integer year, Integer yearTo, LocalDate datePurchased, LocalDate datePurchasedTo, Double price, Double priceTo) {
        this.make = make;
        this.modelList = modelList;
        this.year = year;
        this.yearTo = yearTo;
        this.datePurchased = datePurchased;
        this.datePurchasedTo = datePurchasedTo;
        this.price = price;
        this.priceTo = priceTo;
        System.out.println("com.example.CarDealerShip.Models.carSearchDTO.<init>(ALL aRG)");
    }

    public CarSearchDTO() {
        
        System.out.println("com.example.CarDealerShip.Models.carSearchDTO.<init>()");
    }
    
    
}
