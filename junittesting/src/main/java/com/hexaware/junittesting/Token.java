package com.hexaware.junittesting;

import java.math.BigDecimal;

public record Token(TokenType type, String lexeme, BigDecimal number) {
    public static Token num(BigDecimal n) { return new Token(TokenType.NUMBER, n.toPlainString(), n); }
    public static Token op(String s) { return new Token(TokenType.OP, s, null); }
    public static Token lparen() { return new Token(TokenType.LPAREN, "(", null); }
    public static Token rparen() { return new Token(TokenType.RPAREN, ")", null); }
}
