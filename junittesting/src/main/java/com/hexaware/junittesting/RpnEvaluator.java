package com.hexaware.junittesting;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.List;

public class RpnEvaluator {
	
	private final MathContext mc;
	public RpnEvaluator(MathContext mc) {
		this.mc = mc;
		// TODO Auto-generated constructor stub
	}
	public BigDecimal eval(List<Token> rpn) {
		//
		
		var st = new ArrayDeque<BigDecimal>();
		for(var t : rpn) {
			switch (t.type()) {
			case NUMBER -> st.push(t.number());
			case OP->{
				if(st.size()<2) throw new IllegalArgumentException("insufficient operands");
				
				var b = st.pop(); var a = st.pop();
				var op = Op.fromSymbol(t.lexeme());
				st.push(op.fn.apply(a, b, mc));
			}
			
			
			
			default ->
			throw new IllegalArgumentException("Unexpected value: " );
			}
		}
		if(st.size()!=1) throw new IllegalArgumentException("Invalid expression");
		
		return st.pop().round(mc);
		
	}

}
