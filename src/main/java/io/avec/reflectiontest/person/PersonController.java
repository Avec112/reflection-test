package io.avec.reflectiontest.person;

import io.avec.reflectiontest.classification.ClassifiedData;
import io.avec.reflectiontest.user.User;
import io.avec.reflectiontest.user.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
@RestController
public class PersonController {

    private PersonService service;
    private PersonRepository repository; // no good
    private UserRepository userRepository;

    @Autowired
    public PersonController(PersonService service, PersonRepository repository, UserRepository userRepository) {
        this.service = service;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @RequestMapping("/classified/data/person")
    public List<ClassifiedData> findAllClassified(@RequestHeader("userid") long id) {
        List<ClassifiedData> classifiedDataList = new ArrayList<>();
//        Optional<User> user = userRepository.findById(UUID.fromString(id));
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return service.findAllClassified(user.get());
        } else {
            log.debug("Ingen bruker med id: " + id);
        }

        return classifiedDataList;
    }

    @RequestMapping("/classified/data/person/{id}")
    public ClassifiedData findClassifiedDataForPerson(
            @PathVariable("id") long personId,
            @RequestHeader("userid") long userId,
            @RequestParam(value = "dropRules", required = false) boolean isDropRules
    ) throws NotFoundException {

        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new NotFoundException("User with id " + userId + " where not found."));
        ClassifiedData classifiedData = service.findPersonById(personId, user.get());
        if(isDropRules) {
            classifiedData.dropRules();
        }
        log.debug("This is a classified version of a Person object: {}", classifiedData);
        return classifiedData;
    }

    @RequestMapping("/classified/person/{id}")
    public Person findPersonClassified(
            @PathVariable("id") long personId,
            @RequestHeader("userid") long userId
    ) throws NotFoundException {

        ClassifiedData classifiedData = findClassifiedDataForPerson(personId, userId, false);
        Person classifiedPerson = service.populate(Person.class, classifiedData.getValues());
        log.debug("This is a classified version of a Person object: {}", classifiedPerson);
        return classifiedPerson;
    }

    @RequestMapping("/unclassified/person")
    public List<Person> findAllUnclassified() {
        return (List<Person>) repository.findAll();
    }
}
