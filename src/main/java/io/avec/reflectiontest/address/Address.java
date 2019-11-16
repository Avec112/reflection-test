package io.avec.reflectiontest.address;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by avec112 on 12.11.19.
 */
@Entity
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String streetAdress;
    private String zipCode;
    private String city;
    private String country;

    public Address(String streetAdress, String zipCode, String city, String country) {
        this.streetAdress = streetAdress;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }
}
