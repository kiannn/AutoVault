package com.example.CarDealerShip.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "documnets")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder 
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(name="full name", columnDefinition = "VARCHAR(255) COLLATE utf8_bin")
    String name;
    
    @Enumerated(EnumType.STRING)
    FileExtension extension;
    
    String type;

    Long size;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    byte[] data;

    @JoinColumns({
    @JoinColumn(name = "car ID", referencedColumnName = "itemNo")})
    @ManyToOne(fetch = FetchType.LAZY)
    Car car;
 
    @Override
    public String toString() {
        return "Documents{" + "id=" + id + ", name=" + name + ", FileExtension=" + extension + '}';
    }

}
