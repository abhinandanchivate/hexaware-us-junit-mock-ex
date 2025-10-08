package com.hexaware.junittesting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class ShuntingYard {
    public List<Token> toRpn(List<Token> tokens) {
        var output = new ArrayList<Token>();
        var ops = new ArrayDeque<Token>();

        for (var t : tokens) {
            switch (t.type()) {
                case NUMBER -> output.add(t);
                case OP -> {
                    var op1 = Op.fromSymbol(t.lexeme());
                    while (!ops.isEmpty() && ops.peek().type() == TokenType.OP) {
                        var op2 = Op.fromSymbol(ops.peek().lexeme());
                        if (op2.precedence >= op1.precedence) output.add(ops.pop());
                        else break;
                    }
                    ops.push(t);
                }
                case LPAREN -> ops.push(t);
                case RPAREN -> {
                    while (!ops.isEmpty() && ops.peek().type() != TokenType.LPAREN) output.add(ops.pop());
                    if (ops.isEmpty() || ops.peek().type() != TokenType.LPAREN) throw new IllegalArgumentException("Mismatched parens");
                    ops.pop(); 
                }
            }
        }
        while (!ops.isEmpty()) {
            var t = ops.pop();
            if (t.type() == TokenType.LPAREN || t.type() == TokenType.RPAREN) throw new IllegalArgumentException("Mismatched parens");
            output.add(t);
        }
        return output;
    }
}

/*
 * In the Shunting Yard algorithm, you need an operator stack:
 * 
 * ops.push(t) → push operator or left parenthesis
 * 
 * ops.pop() → pop operators when precedence rules are met
 * 
 * ops.peek() → check the top operator without removing
 * 
 * ArrayDeque supports all of these efficiently:
 * 
 * push() = addFirst()
 * 
 * pop() = removeFirst()
 * 
 * peek() = peekFirst()
 * 
 */

// this is the impl of Dijkstra's shunting algo.
// 2 1. infix
// 3 + 5 * 2 ==> 13
// 1. multiplication ==> ? ==> u know the precedence rule.
// while doing this op through a program ? ==> u have to write the that rule
// book ==> that rule book will be used by computer / program
// to per form the operation.

// postfix : reverse polish notation ==> RPN.
// infix : 3+5 * 2 is ambiguous unless and until u know the rulebook

// * , + operator precedence :
// associations / placement of executions / evaluation of maths exp: L-> R ==>
// operator precedence ?

// 3 + 5 * 2 ==> 13
// infix ==> postfix ==> conversions.
// stack : DS
// push ==> 3 (stores internally) => 3
// push ====> + (op) ==>
// 3 + 4 * 2 / ( 1 - 5 )
