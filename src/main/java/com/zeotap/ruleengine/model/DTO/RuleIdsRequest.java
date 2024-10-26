package com.zeotap.ruleengine.model.DTO;

import java.util.List;

public class RuleIdsRequest {
    private List<Integer> ruleIds;

    // Getters and Setters
    public List<Integer> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<Integer> ruleIds) {
        this.ruleIds = ruleIds;
    }
}
