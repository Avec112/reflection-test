package io.avec.reflectiontest.classification;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificationRuleRepository extends CrudRepository<ClassificationRule, Long> {
    List<ClassificationRule> findByClassName(String className);
}
