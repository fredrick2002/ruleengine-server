package com.zeotap.ruleengine.service;

import com.zeotap.ruleengine.model.Node;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class AstNodeService {

    // Array of supported comparison operators
    private final String[] operators = {"=", ">", "<", ">=", "<="};

    // Function to split and create a comparison node (e.g., age > 30)
    private Node create_operand(String condition) {
        for (String op : operators) {
            if (condition.contains(op)) {
                String[] parts = condition.split(op);
                if (parts.length == 2) {
                    return new Node(
                            "operator", op,
                            new Node("operand", parts[0].trim()),  // Left operand
                            new Node("operand", parts[1].trim())   // Right operand
                    );
                }
            }
        }
        // If no operator found, treat the whole condition as a single operand
        return new Node("operand", condition.trim());
    }

    public Node create_rule(String rule) {
        Stack<Node> stack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        StringBuilder currentOperand = new StringBuilder();

        // Normalize spaces
        rule = rule.replaceAll("\\s+", " ").trim();

        for (int i = 0; i < rule.length(); i++) {
            char c = rule.charAt(i);

            if (c == '(') {
                if (currentOperand.length() > 0) {
                    stack.push(create_operand(currentOperand.toString().trim()));
                    currentOperand.setLength(0);
                }
                operatorStack.push("(");
            } else if (c == ')') {
                if (currentOperand.length() > 0) {
                    stack.push(create_operand(currentOperand.toString().trim()));
                    currentOperand.setLength(0);
                }
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    String operator = operatorStack.pop();
                    Node right = stack.pop();
                    Node left = stack.pop();
                    stack.push(new Node("operator", operator, left, right));
                }
                operatorStack.pop(); // Remove the '(' from the stack
            } else if (c == ' ') {
                continue; // Skip spaces
            } else if (c == 'A' || c == 'O') {
                if (currentOperand.length() > 0) {
                    stack.push(create_operand(currentOperand.toString().trim()));
                    currentOperand.setLength(0);
                }
                // Handle "AND" and "OR"
                if (c == 'A' && (i + 2 < rule.length() && rule.substring(i, i + 3).equals("AND"))) {
                    operatorStack.push("AND");
                    i += 2; // Skip over "AND"
                } else if (c == 'O' && (i + 1 < rule.length() && rule.substring(i, i + 2).equals("OR"))) {
                    operatorStack.push("OR");
                    i += 1; // Skip over "OR"
                }
            } else {
                currentOperand.append(c); // Build the operand string
            }
        }

        // Push the last operand if available
        if (currentOperand.length() > 0) {
            stack.push(create_operand(currentOperand.toString().trim()));
        }

        // Process remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            String operator = operatorStack.pop();
            Node right = stack.pop();
            Node left = stack.pop();
            stack.push(new Node("operator", operator, left, right));
        }

        return stack.isEmpty() ? null : stack.pop(); // Return the root of the AST
    }
}
