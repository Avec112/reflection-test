package io.avec.reflectiontest;

import io.avec.reflectiontest.address.Address;
import io.avec.reflectiontest.address.AddressRepository;
import io.avec.reflectiontest.classification.*;
import io.avec.reflectiontest.person.Person;
import io.avec.reflectiontest.person.PersonRepository;
import io.avec.reflectiontest.person.Sex;
import io.avec.reflectiontest.user.User;
import io.avec.reflectiontest.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootApplication
public class ReflectionTestApplication  {

    private final ClassificationService classificationService;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ClassificationRuleRepository classificationRuleRepository;

    public ReflectionTestApplication(ClassificationService classificationService, PersonRepository personRepository, AddressRepository addressRepository, UserRepository userRepository, ClassificationRuleRepository classificationRuleRepository) {
        this.classificationService = classificationService;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.classificationRuleRepository = classificationRuleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReflectionTestApplication.class, args);
    }


    // populate database with some data
    @Bean
    public CommandLineRunner populateDatabase() {
        return (args -> {
            // create system user
            User user = userRepository.save(new User("user1", Classification.UNCLASSIFIED));
            log.debug("User created: {}", user);
            user = userRepository.save(new User("user2", Classification.RESTRICTED));
            log.debug("User created: {}", user);
            user = userRepository.save(new User("user3", Classification.CONFIDENTIAL));
            log.debug("User created: {}", user);
            user = userRepository.save(new User("user4", Classification.SECRET));
            log.debug("User created: {}", user);

            // Create person 1
            Address address1 = addressRepository.save(new Address("Parkveien 12", "1234", "Oslo", "Norway"));
            Person person1 = personRepository.save(getPerson1(address1));
            log.debug("created {}", person1);

            // Create person 2
            Address address2 = addressRepository.save(new Address("Bakerstreet 341", "06255", "Los Angeles", "USA"));
            Person person2 = personRepository.save(getPerson2(address2));
            log.debug("created {}", person2);

            // Create rules
            var personRules = getPersonRules();
            log.debug("created rules {}", personRules);

        });
    }

    private List<ClassificationRule> getPersonRules() {

        // Create default rules. All with Classification.NO_ACCESS
        classificationService.createAndSaveDefaultClassificationRules(Person.class);

        // Define specific rules for class Person
        modifyRule("getId", Classification.UNCLASSIFIED);
        modifyRule("getNationality", Classification.RESTRICTED);
        modifyRule("getNickName", Classification.RESTRICTED);
        modifyRule("getDateOfBirth", Classification.CONFIDENTIAL);
        modifyRule("getFirstName", Classification.CONFIDENTIAL);
        modifyRule("getSex", Classification.CONFIDENTIAL);
        modifyRule("getAddress", Classification.SECRET);
        modifyRule("getLastName", Classification.SECRET);

        // return all rules for Person
        return classificationRuleRepository.findByClassificationRuleIdClassName(Person.class.getName());
    }

    private void modifyRule(String methodName, Classification classification) {
        Optional<ClassificationRule> rule = classificationRuleRepository.findById(new ClassificationRuleId(Person.class.getName(), methodName));
        rule.ifPresent((theRule) -> {
            theRule.setClassification(classification);
            classificationRuleRepository.save(theRule);
        });
    }


    // example data. Person implements UnclassifiedData
    private Person getPerson1(Address address) {
        Person personData = new Person();
        personData.setFirstName("Kalle");
        personData.setLastName("Anka");
        personData.setNickName("Ducky boy");
        personData.setNationality("Sweden");
        personData.setAddress(address);
        personData.setDateOfBirth(LocalDate.of(1980, 1, 20));
        personData.setSex(Sex.MALE);
        personData.setNewMethod("I \"forgot\" to set clearence level. Should stay hidden though :-)");
        return personData;
    }


    private Person getPerson2(Address address) {
        Person personData = new Person();
        personData.setFirstName("Dolly");
        personData.setLastName("Duck");
        personData.setNickName("Doll");
        personData.setNationality("USA");
        personData.setAddress(address);
        personData.setDateOfBirth(LocalDate.of(1937, 12, 1));
        personData.setSex(Sex.FEMALE);
        personData.setNewMethod("I \"forgot\" to set clearence level. Should stay hidden though :-)");
        return personData;
    }

}
