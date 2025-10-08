package com.hexaware.junittesting;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public enum Op {
    ADD("+", 1, (a,b,mc) -> a.add(b, mc)),
    SUB("-", 1, (a,b,mc) -> a.subtract(b, mc)),
    MUL("*", 2, (a,b,mc) -> a.multiply(b, mc)),
    DIV("/", 2, (a,b,mc) -> {
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new MathException("Divide by zero");
        // Scale/rounding handled by MathContext
        return a.divide(b, mc);
    });

    public final String symbol;
    public final int precedence;

    @FunctionalInterface
    interface DecimalOp {
        BigDecimal apply(BigDecimal a, BigDecimal b, MathContext mc);
    }

    public final DecimalOp fn;

    Op(String symbol, int precedence, DecimalOp fn) {
        this.symbol = symbol; this.precedence = precedence; this.fn = fn;
    }

    public static Op fromSymbol(String s) {
        for (Op o : values()) if (o.symbol.equals(s)) return o;
        throw new IllegalArgumentException("Unknown operator: " + s);
    }
}
