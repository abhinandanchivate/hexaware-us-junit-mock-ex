package com.hexaware.junittesting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public List<Token> tokenize(String expr) {
    	// validation of expression.
    	// does it have null , "" blank cheking these aspects 
    	
        if (expr == null || expr.isBlank()) throw new IllegalArgumentException("Empty expression");
        var out = new ArrayList<Token>();// to hold the expression. ==> to evaluate the result
        // 5-2 Token(5 ==> num) , Token(- op ==> string) , Token(2 ==> number).
        int i = 0, n = expr.length();
        // we will start traversal from 0th index to length -1 
        
        while (i < n) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }

            if (c == '(') { out.add(Token.lparen()); i++; continue; }
            if (c == ')') { out.add(Token.rparen()); i++; continue; }

            if ("+-*/".indexOf(c) >= 0) { // positive value , negative , 0 : ? 
                out.add(Token.op(String.valueOf(c))); i++; continue;
            }

            if (Character.isDigit(c) || c == '.') {
                int start = i; boolean dot = (c == '.');
                i++;
                while (i < n) {
                    char d = expr.charAt(i);
                    if (Character.isDigit(d)) { i++; continue; }
                    if (d == '.' && !dot) { dot = true; i++; continue; }
                    break;
                }
                var numStr = expr.substring(start, i);
                out.add(Token.num(new BigDecimal(numStr)));
                continue;
            }

            throw new IllegalArgumentException("Bad char at " + i + ": '" + c + "'");
        }
        return out;
    }
} // expression .10+5 (5*2)+3
