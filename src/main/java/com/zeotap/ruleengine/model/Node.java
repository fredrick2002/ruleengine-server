// src/main/java/com/example/ruleengine/model/Node.java

package com.zeotap.ruleengine.model;

import java.util.Objects;

public class Node {
    private String type; // "operator" or "operand"
    private String value; // the value of the operand or operator
    private Node left; // left child node
    private Node right; // right child node

    // Constructor for operand nodes
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    // Constructor for operator nodes with left and right children
    public Node(String type, String value, Node left, Node right) {
        this.type = type;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

//    public boolean isLeaf() {
//        return left == null && right == null;
//    }

    @Override
    public String toString() {
        return "{" +
                "\"type\": \"" + type + "\"" +
                ", \"value\": \"" + value + "\"" +
                ", \"left\": " + (left != null ? left.toString() : "null") +
                ", \"right\": " + (right != null ? right.toString() : "null") +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return type.equals(node.type) && value.equals(node.value);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + value.hashCode();
    }
}
