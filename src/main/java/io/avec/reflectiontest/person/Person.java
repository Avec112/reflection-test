package io.avec.reflectiontest.person;

import io.avec.reflectiontest.address.Address;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by avec112 on 12.11.19.
 */
@Data
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate dateOfBirth;
    private String nationality;
    @ManyToOne
    private Address address;
    private Sex sex;
    private String newMethod;

}
