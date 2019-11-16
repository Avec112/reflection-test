package io.avec.reflectiontest.classification;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * Created by avec112 on 12.11.19.
 */
@Slf4j
@Entity
@Data
@NoArgsConstructor
public class ClassificationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String className;
    private String methodName;
    private Classification classification;

    public ClassificationRule(String className, String methodName, Classification classification) {
        this.className = className;
        this.methodName = methodName;
        this.classification = classification;
    }
}
