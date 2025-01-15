
package com.example.CarDealerShip.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class NHTSAResponse {

    public int Count;

    @JsonProperty("Results")
    public List<Car> data;
}
