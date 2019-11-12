package io.avec.reflectiontest.classification;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by avec112 on 12.11.19.
 */
@Getter
@ToString
public
class ClassificationRules {
    private String referenceName;
    private Map<String,Classification> classificationMap;

    ClassificationRules(String referenceName, Map<String, Classification> classificationMap) {
        this.referenceName = referenceName;
        this.classificationMap = classificationMap;
    }

}
