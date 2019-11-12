package io.avec.reflectiontest;

import io.avec.reflectiontest.classification.*;
import io.avec.reflectiontest.data.Address;
import io.avec.reflectiontest.data.Person;
import io.avec.reflectiontest.data.Sex;
import io.avec.reflectiontest.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class ReflectionTestApplication implements CommandLineRunner {

    @Autowired
    private ClassificationService classificationService;

    public static void main(String[] args) {
        SpringApplication.run(ReflectionTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Unclassified data = all data visible
        UnclassifiedData unclassifiedData = getPerson();

        // Classification Rules for current data (person in this example)
        ClassificationRules classificationRules = classificationService.getClassificationSchema(unclassifiedData);

        // a user of the system
        User user = new User("user1", Classification.B);

        // Classified data = data may be hidden from the user based on user classification
        ClassifiedData classifiedData = classificationService.getClassifiedData(unclassifiedData, classificationRules, user);

        // print results and compare with original data
        classificationService.printComparison(classificationRules, unclassifiedData, classifiedData);

    }

    // example data. Person implements UnclassifiedData
    private Person getPerson() {
        Person personData = new Person();
        personData.setFirstName("Arne");
        personData.setLastName("And");
        personData.setNickName("Kråka");
        personData.setNationality("Norsk");
        personData.setAddress(new Address("Parkveien 12", "1234", "Oslo"));
        personData.setDateOfBirth(LocalDate.of(1980, 1, 20));
        personData.setSex(Sex.MALE);
        personData.setNewMethod("I \"forgot\" to set clearence level. Should stay hidden though :-)");
        return personData;
    }

}
