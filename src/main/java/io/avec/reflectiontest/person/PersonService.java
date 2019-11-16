package io.avec.reflectiontest.person;

import io.avec.reflectiontest.Populator;
import io.avec.reflectiontest.classification.ClassificationService;
import io.avec.reflectiontest.classification.ClassifiedData;
import io.avec.reflectiontest.user.User;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PersonService extends Populator {

    private PersonRepository personRepository;
    private ClassificationService classificationService;

    @Autowired
    public PersonService(PersonRepository personRepository, ClassificationService classificationService) {
        this.personRepository = personRepository;
        this.classificationService  = classificationService;
    }

    List<ClassifiedData> findAllClassified(User user) {
        var classifiedPersons = new ArrayList<ClassifiedData>();
        var unclassifiedPersons = personRepository.findAll();
        var classificationRules = classificationService.getClassificationRules(Person.class.getName());

        unclassifiedPersons.forEach(unlassified -> {
            ClassifiedData classifiedData = classificationService.getClassifiedData(unlassified, classificationRules, user);
            log.debug("Classified: {}", classifiedData);
            classifiedPersons.add(classifiedData);
        });

        return classifiedPersons;
    }

    ClassifiedData findPersonById(long id, User user) throws NotFoundException {
        Optional<Person> unclassifiedPerson = personRepository.findById(id);

        unclassifiedPerson.orElseThrow(() -> new NotFoundException("No person with id " + id + " where found."));

        var classificationRules = classificationService.getClassificationRules(Person.class.getName());
        ClassifiedData classifiedData = classificationService.getClassifiedData(unclassifiedPerson.get(), classificationRules, user);
        log.debug("Classified: {}", classifiedData);
        return classifiedData;
    }



}
