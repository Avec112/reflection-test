package io.avec.reflectiontest.classification;

import io.avec.reflectiontest.user.User;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by avec112 on 12.11.19.
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassifiedData {
    private User user;
    private String className;
    private Map<String,Object> values;
    private List<ClassificationRule> classificationRules;

    public void dropRules() {
        classificationRules.clear();
    }
}
