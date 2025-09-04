package com.example.CarDealerShip.Models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
 
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    Owner carOwner; 
 
    String authority; 

    @Override
    public String toString() { 
        return "Authorities{" + "id=" + id + ", carOwner=" + (carOwner!=null? carOwner.getUsername():null) + ", authority=" + authority + '}';
    }

  

}
