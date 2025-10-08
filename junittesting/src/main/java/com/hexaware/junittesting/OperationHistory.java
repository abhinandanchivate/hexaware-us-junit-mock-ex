package com.hexaware.junittesting;


import java.math.BigDecimal;
import java.util.ArrayDeque;

public class OperationHistory {
    private final ArrayDeque<BigDecimal> stack = new ArrayDeque<>();

    public void push(BigDecimal state) { stack.push(state); }
    public BigDecimal undo(BigDecimal current) {
        if (stack.isEmpty()) return current;
        return stack.pop();
    }
    public int size(){ return stack.size(); }
    public void clear(){ stack.clear(); }
}
