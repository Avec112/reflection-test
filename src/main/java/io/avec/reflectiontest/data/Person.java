package io.avec.reflectiontest.data;

import io.avec.reflectiontest.classification.UnclassifiedData;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by avec112 on 12.11.19.
 */
@Data
public
class Person implements UnclassifiedData {
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate dateOfBirth;
    private String nationality;
    private Address address;
    private Sex sex;
    private String newMethod;

}
