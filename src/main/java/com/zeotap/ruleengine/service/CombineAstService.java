package com.zeotap.ruleengine.service;

import com.zeotap.ruleengine.model.Node;
import com.zeotap.ruleengine.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CombineAstService {

    private static final Logger logger = LoggerFactory.getLogger(CombineAstService.class);

    @Autowired
    private NodeRepository nodeRepository;

    public Node combineAndOptimizeAst(Node ast1, Node ast2) {
        if (ast1 == null) return ast2;
        if (ast2 == null) return ast1;

        // Combine both ASTs under an AND operator
        Node combinedNode = new Node("operator", "AND", ast1, ast2);
        logger.debug("Combined AST before optimization: {}", nodeToString(combinedNode));

        // Optimize the combined AST to reduce nesting and remove duplicates
        Node optimizedNode = optimizeAst(combinedNode);
        logger.debug("Optimized AST: {}", nodeToString(optimizedNode));
        return optimizedNode; // Return the optimized combined AST
    }

    public Node optimizeAst(Node node) {
        if (node == null) return null;

        // If the node is an AND operator, flatten its children
        if ("AND".equals(node.getValue())) {
            List<Node> flattenedChildren = flattenChildren(node);
            // Remove duplicates
            Set<String> uniqueConditions = new HashSet<>();
            List<Node> deduplicatedChildren = new ArrayList<>();

            for (Node child : flattenedChildren) {
                String conditionKey = nodeToString(child); // Use the string representation as a unique key
                if (uniqueConditions.add(conditionKey)) {
                    deduplicatedChildren.add(child); // Only add if it's unique
                }
            }

            if (deduplicatedChildren.isEmpty()) return null; // Prune if there are no valid children
            if (deduplicatedChildren.size() == 1) return deduplicatedChildren.get(0); // Return single child if exists

            // Create a new AND node with flattened and deduplicated children
            return createCombinedAndNode(deduplicatedChildren);
        }

        // Optimize left and right subtrees
        node.setLeft(optimizeAst(node.getLeft()));
        node.setRight(optimizeAst(node.getRight()));

        return node; // Return as-is if no optimizations apply
    }

    private List<Node> flattenChildren(Node node) {
        List<Node> children = new ArrayList<>();
        collectChildren(node, children);
        return children;
    }

    private void collectChildren(Node node, List<Node> children) {
        if (node == null) return;

        if ("operator".equals(node.getType()) && "AND".equals(node.getValue())) {
            collectChildren(node.getLeft(), children);
            collectChildren(node.getRight(), children);
        } else {
            children.add(node); // It's an operand, add it to the list
        }
    }

    private Node createCombinedAndNode(List<Node> children) {
        // Initialize a root node for the combined AND operation
        Node newAndNode = new Node("operator", "AND");

        // Track the last added node
        Node lastAdded = null;

        // Combine all children into a new AND node
        for (Node child : children) {
            if (lastAdded == null) {
                newAndNode.setLeft(child); // First child
            } else {
                Node newChild = new Node("operator", "AND", lastAdded, child);
                lastAdded = newChild; // Update last added node
                newAndNode.setRight(lastAdded); // Set right as the last added node
            }
            lastAdded = child; // Update lastAdded to the current child
        }

        return newAndNode;
    }

    private String nodeToString(Node node) {
        if (node == null) return "null";
        if ("operand".equals(node.getType())) {
            return node.getValue();
        } else {
            String leftString = nodeToString(node.getLeft());
            String rightString = nodeToString(node.getRight());
            return node.getValue() + "(" + leftString + ", " + rightString + ")";
        }
    }
}
