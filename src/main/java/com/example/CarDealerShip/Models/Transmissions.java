package com.example.CarDealerShip.Models;

import lombok.Getter;

@Getter
public enum Transmissions {

    AWD("AWD"),
    FWD("FWD"),
    RWD("RWD"),
    _4WD("4WD");

    String tr;

    private Transmissions(String tr) {
        this.tr = tr;
    }  
    
}
