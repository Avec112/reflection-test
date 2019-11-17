package io.avec.reflectiontest.classification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationRuleId implements Serializable {
    private String className;
    private String methodName;
}
