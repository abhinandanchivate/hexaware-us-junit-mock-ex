package com.hexaware.junittesting;

import java.math.BigDecimal;

public class Memory {

	private BigDecimal value = BigDecimal.ZERO;
	public BigDecimal recall() {
		return value;
	}
	public void store(BigDecimal v) {
		this.value = v;
		
	}
	public void clear() {
		this.value = BigDecimal.ZERO;
	}
	
}
