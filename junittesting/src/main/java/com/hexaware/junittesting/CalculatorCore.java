package com.hexaware.junittesting;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalculatorCore {
	
	
	
	private final MathContext mc;

	public CalculatorCore(MathContext mc) {
		this.mc= mc;
		// TODO Auto-generated constructor stub
	}
	
	//  math ops on BD==> only on BD 
	// they do have thier own methods to perform the ops;
	public BigDecimal add(BigDecimal a , BigDecimal b) {
		return a.add(b);
				
	}
	public BigDecimal sub(BigDecimal a, BigDecimal b ) {
		return a.subtract(b);
	}
	
	public BigDecimal mult(BigDecimal a, BigDecimal b ) {
		return a.multiply(b);
	}
	public BigDecimal div(BigDecimal a, BigDecimal b ) {
		if(b.signum()==0) throw new MathException("Divide by Zero");
		return a.divide(b, mc);
	}
}
