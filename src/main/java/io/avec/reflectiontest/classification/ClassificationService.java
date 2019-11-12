package io.avec.reflectiontest.classification;

import io.avec.reflectiontest.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by avec112 on 12.11.19.
 */
@Slf4j
@Service
public class ClassificationService {


    /**
     * Example putting classification pr field on a class unclassifiedData
     * @param unclassifiedData the class where fields will be classified
     * @return classified representation of inputted object
     */
    public ClassificationRules getClassificationSchema(UnclassifiedData unclassifiedData) {
        Map<String,Classification> matrix = new HashMap<>();

        // find all declared methods for unclassifiedData
        Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(unclassifiedData.getClass());

        // Populate all getters with Classification A
        Arrays.stream(declaredMethods)
                .filter(e -> e.getName().startsWith("get")) // only those starting with "get"
                .sorted(Comparator.comparing(Method::getName)) // sort by method name
                .forEach(e -> matrix.put(e.getName(), Classification.NO_ACCESS)); // ensure hidden

        // Here you could present content in UI
        // A security manager could define correct setting for current unclassifiedData
        // Then return it.
        // lets make an example where all field is classified
        matrix.replace("getNickName", Classification.A);
        matrix.replace("getFirstName", Classification.C);
        matrix.replace("getLastName", Classification.C);
        matrix.replace("getDateOfBirth", Classification.C);
        matrix.replace("getNationality", Classification.B);
        matrix.replace("getSex", Classification.C);
        matrix.replace("getAddress", Classification.D);

        return new ClassificationRules(unclassifiedData.getClass().getName(), matrix);
    }


    public ClassifiedData getClassifiedData(UnclassifiedData unclassifiedData, ClassificationRules classificationRules, User user) {
        assert unclassifiedData != null;
        assert classificationRules != null;
        assert user != null;

        Map<String,Object> allowedValues = new HashMap<>();
        var userClassification = user.getSecurityClearence();
        var classificationMap = classificationRules.getClassificationMap();

        classificationMap.forEach((methodName, classification) -> {
            var method = ReflectionUtils.findMethod(unclassifiedData.getClass(), methodName);
            if(userClassification.isHigherOrEqualTo(classification)) {
                var value = getObject(unclassifiedData, method, methodName);
                allowedValues.put(methodName, value);
            } else {
                allowedValues.put(methodName, "-");
            }
        });

        return new ClassifiedData(user, classificationRules.getReferenceName(), allowedValues);

    }

    public void printComparison(ClassificationRules classificationRules, UnclassifiedData unclassifiedData, ClassifiedData classifiedData) {
        System.out.println(classifiedData.getUser());

        System.out.println(String.format("%-15s|%-12s|Gradert info (%s)    |%s", "Metodenavn", "Gradering", classifiedData.getUser().getSecurityClearence(), "Original info"));
        System.out.println("---------------------------------------------------------------");

        var classificationMap = classificationRules.getClassificationMap();

        classificationMap.forEach((methodName, classification) -> {

            var rule = classificationRules.getClassificationMap().get(methodName);
            var method = ReflectionUtils.findMethod(unclassifiedData.getClass(), methodName);

            Object v1 = getObject(unclassifiedData, method, methodName);

            var v2 = classifiedData.getAllowedValues().get(methodName);

            System.out.println(String.format("%-15s|%-12s|%-20s|%s", methodName, rule, v2, v1));

        });
    }

    private static Object getObject(UnclassifiedData unclassifiedData, Method method, String methodName) {
        if(method == null) {
            throw new NullPointerException(String.format("Method name %s not found for class %s.", methodName, unclassifiedData.getClass().getName()));
        }
        Object v1 = null;

        try {
             v1 = method.invoke(unclassifiedData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Exception.", e);
        }
        return v1;
    }
}
