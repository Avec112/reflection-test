package io.avec.reflectiontest.classification;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Created by avec112 on 12.11.19.
 */
@Slf4j
@Entity
@Data
@NoArgsConstructor
public class ClassificationRule {
    @EmbeddedId
    private ClassificationRuleId classificationRuleId;
    private Classification classification;

    public ClassificationRule(ClassificationRuleId classificationRuleId, Classification classification) {
        this.classificationRuleId = classificationRuleId;
        this.classification = classification;
    }
}
