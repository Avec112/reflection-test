package io.avec.reflectiontest.classification;

import io.avec.reflectiontest.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Created by avec112 on 12.11.19.
 */
@Data
@AllArgsConstructor
public class ClassifiedData {
    private User user;
    private String referenceObjectName;
    private Map<String,Object> allowedValues;
}
