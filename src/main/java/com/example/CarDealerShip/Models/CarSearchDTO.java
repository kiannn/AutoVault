package com.example.CarDealerShip.Models;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarSearchDTO {

    String make;
    
    List<String> modelList;

    Integer year;
    Integer yearTo;

    @DateTimeFormat(pattern = "yyyy-MMM-dd", fallbackPatterns ={"yyyy-MM-dd","dd/MMM/yyyy","dd.MM.yyyy","MM/dd/yyyy","dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM"})
    LocalDate datePurchased ;
    
    @DateTimeFormat(pattern = "yyyy-MMM-dd", fallbackPatterns ={"yyyy-MM-dd","dd/MMM/yyyy","dd.MM.yyyy","MM/dd/yyyy","dd/MM/yyyy","yyyy/MM/dd","yyyy/dd/MM"})
    LocalDate datePurchasedTo;

    Double price;
    Double priceTo;
    
    
}
