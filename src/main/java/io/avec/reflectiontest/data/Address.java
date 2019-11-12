package io.avec.reflectiontest.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by avec112 on 12.11.19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Address {
    private String streetAdress;
    private String zipCode;
    private String city;
}
