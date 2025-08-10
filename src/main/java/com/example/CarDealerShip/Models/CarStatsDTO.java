
package com.example.CarDealerShip.Models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarStatsDTO {

    LocalDate datePurchased;
    Double price;
    Double hp;

}
