package com.zeotap.ruleengine.repository;

import com.zeotap.ruleengine.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    Rule findById(long id);
}
