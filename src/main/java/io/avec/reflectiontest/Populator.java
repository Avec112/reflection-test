package io.avec.reflectiontest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class Populator {

    public <T> T populate(Class<T> clazz, Map<String, Object> values) {

        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance of class " + clazz.getName());
        }

        Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(clazz);
        for (Method method : declaredMethods) {
            if (method.getName().startsWith("set")) {
                var setMethod = method.getName();
                var getMethod = setMethod.replace("set", "get");
                var value = values.get(getMethod); // find value from map
                if (value != null) {
                    try {
                        method.invoke(instance, value);
                    } catch (Exception e) {
                        String msg = String.format("Could not invoke class %s method %s", clazz.getName(), setMethod);
                        log.debug(msg);
                        throw new RuntimeException(msg);
                    }
                }
            }
        }

        log.debug("{} {}", clazz.getName(), instance.toString());
        return instance;
    }
}
