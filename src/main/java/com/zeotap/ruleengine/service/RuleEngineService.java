package com.zeotap.ruleengine.service;

import com.zeotap.ruleengine.model.Node;
import com.zeotap.ruleengine.model.Rule;
import com.zeotap.ruleengine.repository.NodeRepository; // Import NodeRepository
import com.zeotap.ruleengine.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleEngineService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private NodeRepository nodeRepository;

    // Inject AstNodeService for AST creation
    @Autowired
    private AstNodeService astNodeService;

    @Autowired
    private AstEvaluationService astEvaluationService;

    @Autowired
    private CombineAstService combineAstService;

    public Node getRuleAsAST(long ruleId) {
        Rule rule = ruleRepository.findById(ruleId);
        if (rule != null) {
            Node astNode = astNodeService.create_rule(rule.getRuleString()); // Use the new service to create AST
            nodeRepository.saveNode((int) ruleId, astNode); // Store the AST in NodeRepository
            return astNode; // Return the AST
        }
        return null; // Handle the case where the rule is not found
    }

    /**
     * Evaluate the rule based on ruleId and user data.
     *
     * @param ruleId The ID of the rule to evaluate.
     * @param data   The user data to be checked against the rule.
     * @return true if the user meets the rule criteria, false otherwise.
     */
    public boolean evaluateRule(int ruleId, Map<String, Object> data) {
        // Retrieve the rule (AST) by ruleId from the NodeRepository
        System.out.println(ruleId);
        Node astNode = nodeRepository.getNode(ruleId);

        // If the rule does not exist, throw an error or return false
        if (astNode == null) {
            throw new IllegalArgumentException("Rule with ID " + ruleId + " does not exist.");
        }

        // Evaluate the user data against the rule's AST
        return astEvaluationService.evaluate(astNode, data);
    }



    public Node combineAndOptimizeRules(List<Integer> ruleIds) {
        Node combinedAst = null;

        for (Integer ruleId : ruleIds) {
            Node astNode = nodeRepository.getNode(ruleId);
            if (astNode == null) {
                throw new IllegalArgumentException("Rule ID " + ruleId + " not found in repository.");
            }
            System.out.println("Retrieved AST Node for ID " + ruleId + ": " + astNode);

            // Combine the current AST node with the accumulated AST.
            combinedAst = (combinedAst == null) ? astNode : combineAstService.combineAndOptimizeAst(combinedAst, astNode);
        }
        System.out.println("Combined AST: " + combinedAst);
        return combinedAst;
    }



    // Additional method to convert the AST to JSON for the response
    public Node getAST(String rule) {
        return astNodeService.create_rule(rule); // Use the new service
    }
}
