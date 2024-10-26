package com.zeotap.ruleengine.repository;

import com.zeotap.ruleengine.model.Node;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class NodeRepository {
    private final Map<Integer, Node> nodeStorage = new HashMap<>();

    // Method to save the node with a rule ID
    public void saveNode(int ruleId, Node node) {
        nodeStorage.put(ruleId, node);
    }

//    // Method to retrieve the node by rule ID
//    public Node getNode(Integer ruleId) {
//        return nodeStorage.get(ruleId);
//    }

    // Method to retrieve the node by rule ID
    public Node getNode(int ruleId) {
        System.out.println("Current nodeStorage contents: " + nodeStorage); // Debugging
        Node node = nodeStorage.get(ruleId);
        if (node == null) {
            System.out.println("Rule ID " + ruleId + " not found in repository");
        } else {
            System.out.println("Retrieved node for rule ID " + ruleId + ": " + node); // Debugging
        }
        return node;
    }
}
