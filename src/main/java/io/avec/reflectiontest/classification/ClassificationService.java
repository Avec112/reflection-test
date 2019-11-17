package io.avec.reflectiontest.classification;

import io.avec.reflectiontest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by avec112 on 12.11.19.
 */
@Slf4j
@Service
public class ClassificationService {

    private ClassificationRuleRepository classificationRuleRepository;

    public ClassificationService(ClassificationRuleRepository classificationRuleRepository) {
        this.classificationRuleRepository = classificationRuleRepository;
    }

    /**
     * @param className the  name of class where fields will be classified
     * @return classified representation of inputted object
     */
    public List<ClassificationRule> getClassificationRules(String className) {
        return classificationRuleRepository.findByClassificationRuleIdClassName(className);
    }

    /**
     * All rules will be the same as provided with classification param
     * @param clazz class to create classification rules for
     * @param classification a enum providing different classifications
     * @return list of the new rules for all getter method
     */
    public <T> List<ClassificationRule> createAndSaveAllSameClassificationRules(Class<T> clazz, Classification classification) {

        final var rules = new ArrayList<ClassificationRule>();

        // find all declared methods for t
        final var declaredMethods = ReflectionUtils.getDeclaredMethods(clazz);

        // Populate all getters with Classification.NO_ACCESS
        Arrays.stream(declaredMethods)
                .filter(method -> method.getName().startsWith("get")) // keep those starting with "get"
                .sorted(Comparator.comparing(Method::getName))
                .forEach(method -> {
                    ClassificationRule rule = new ClassificationRule(new ClassificationRuleId(clazz.getName(), method.getName()), classification);
                    classificationRuleRepository.save(rule);
                    rules.add(rule);
                });

        return rules;
    }

    /**
     * All rules will be Classification.NO_ACCESS
     * @param clazz class to create classification rules for
     * @return list of the new rules for all getter method
     */
    public <T> List<ClassificationRule> createAndSaveDefaultClassificationRules(Class<T> clazz) {
        return createAndSaveAllSameClassificationRules(clazz, Classification.NO_ACCESS);
    }

    public <T> ClassifiedData getClassifiedData(T t, List<ClassificationRule> classificationRules, User user) {
        assert t != null;
        assert classificationRules != null;
        assert user != null;

        final Map<String,Object> allowedValues = new HashMap<>();
        final var userClassification = user.getSecurityClearence();

        classificationRules.forEach(rule -> {
            var methodName = rule.getClassificationRuleId().getMethodName();
            var method = ReflectionUtils.findMethod(t.getClass(), methodName);
            if(userClassification.isHigherOrEqualTo(rule.getClassification())) {
                var value = getObject(t, method, methodName);
                allowedValues.put(methodName, value);
            }
        });
        // Sorting
        classificationRules.sort(Comparator.comparing(ClassificationRule::getClassification));
        return new ClassifiedData(user, t.getClass().getName(), /*populate(t.getClass(), allowedValues),*/ allowedValues, classificationRules);
    }

    private static <T> Object getObject(T t, Method method, String methodName) {
        if(method == null) {
            throw new NullPointerException(String.format("Method name %s not found for class %s.", methodName, t.getClass().getName()));
        }
        Object v1 = null;

        try {
             v1 = method.invoke(t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Exception.", e);
        }
        return v1;
    }
}
