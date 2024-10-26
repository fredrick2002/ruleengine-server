package com.zeotap.ruleengine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeotap.ruleengine.model.DTO.RuleIdsRequest;
import com.zeotap.ruleengine.model.Node;
import com.zeotap.ruleengine.repository.NodeRepository;
import com.zeotap.ruleengine.service.CombineAstService;
import com.zeotap.ruleengine.service.RuleEngineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rules")
public class RuleController {

    @Autowired
    private RuleEngineService ruleEngineService;

    @Autowired
    private CombineAstService combineAstService;

//    private final ObjectMapper objectMapper;


    @PostMapping("/create")
    public ResponseEntity<Node> createRule(@RequestBody String rule) {
        // Remove the JSON wrapper for the rule, extract the rule string
        String ruleString = rule.replaceAll("\\{\\s*\"rule\":\\s*\"(.*?)\"\\s*\\}", "$1").trim();

        Node astNode = ruleEngineService.getAST(ruleString);
        return ResponseEntity.ok(astNode);
    }

    /**
     * API to evaluate the rule based on ruleId and user data.
     *
     * @param ruleId The ID of the rule to evaluate.
     * @param data   The user data to check against the rule.
     * @return true if the user meets the rule criteria, false otherwise.
     */
    @PostMapping("/{ruleId}/evaluate")
    public boolean evaluateRule(
            @PathVariable int ruleId,
            @RequestBody Map<String, Object> data) {
        // Call the evaluation service
        return ruleEngineService.evaluateRule(ruleId, data);
    }


//    @PostMapping("/combine")
//    public String combineRules(@RequestBody RuleIdsRequest ruleIdsRequest) throws JsonProcessingException {
//
//        List<Integer> ruleIds = ruleIdsRequest.getRuleIds();
//        Node combinedAst = combineAstService.combineAndOptimizeRules(ruleIds);
//
//        // Convert the combined AST to JSON and return it
//        return objectMapper.writeValueAsString(combinedAst);
//    }

    @PostMapping("/combine")
    public ResponseEntity<Node> combineRules(@RequestBody RuleIdsRequest ruleIdsRequest) {
        List<Integer> ruleIds = ruleIdsRequest.getRuleIds();
        Node combinedAst = ruleEngineService.combineAndOptimizeRules(ruleIds);
        System.out.println("Final Combined AST: " + combinedAst);
        return ResponseEntity.ok(combinedAst);
    }

    @GetMapping("/{id}")
    public Node getRule(@PathVariable long id) {
        return ruleEngineService.getRuleAsAST(id);
    }
}
