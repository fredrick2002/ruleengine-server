package com.zeotap.ruleengine.service;

import com.zeotap.ruleengine.model.Node;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AstEvaluationService {

    public boolean evaluate(Node astNode, Map<String, Object> data) {
        // Base case: operand node (leaf)
        if (astNode.getType().equals("operand")) {
            return evaluateOperand(astNode.getValue(), data);
        }

        // If the node is an operator, evaluate its children
        if (astNode.getType().equals("operator")) {
            switch (astNode.getValue()) {
                case "AND":
                    return evaluate(astNode.getLeft(), data) && evaluate(astNode.getRight(), data);
                case "OR":
                    return evaluate(astNode.getLeft(), data) || evaluate(astNode.getRight(), data);
                case "=":
                    return evaluateComparison(astNode.getLeft(), astNode.getRight(), data, "=");
                case ">":
                    return evaluateComparison(astNode.getLeft(), astNode.getRight(), data, ">");
                case "<":
                    return evaluateComparison(astNode.getLeft(), astNode.getRight(), data, "<");
                default:
                    throw new IllegalArgumentException("Unknown operator: " + astNode.getValue());
            }
        }

        // Return false if none of the cases are met
        return false;
    }

    private boolean evaluateOperand(String operand, Map<String, Object> data) {
        // Extracts operand value from the data dictionary
        Object value = data.get(operand);
        if (value == null) {
            throw new IllegalArgumentException("Operand not found in data: " + operand);
        }
        // This just checks the presence of the operand in data
        return true; // Leaf node operands only check if they exist
    }

    private boolean evaluateComparison(Node leftNode, Node rightNode, Map<String, Object> data, String operator) {
        // Evaluate the left and right operands for the comparison
        Object leftValue = data.get(leftNode.getValue());
        String rightValue = rightNode.getValue(); // Assuming right side is a constant

        if (leftValue == null) {
            throw new IllegalArgumentException("Operand not found in data: " + leftNode.getValue());
        }

        switch (operator) {
            case "=":
                return leftValue.toString().equals(rightValue);
            case ">":
                return compareNumbers(leftValue, rightValue, ">");
            case "<":
                return compareNumbers(leftValue, rightValue, "<");
            default:
                throw new IllegalArgumentException("Unknown comparison operator: " + operator);
        }
    }

    private boolean compareNumbers(Object leftValue, String rightValue, String operator) {
        // Convert values to numbers and compare
        try {
            double leftNum = Double.parseDouble(leftValue.toString());
            double rightNum = Double.parseDouble(rightValue);

            switch (operator) {
                case ">":
                    return leftNum > rightNum;
                case "<":
                    return leftNum < rightNum;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + operator);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for comparison: " + leftValue + " and " + rightValue);
        }
    }
}
