package com.hexaware.junitesting.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.MathContext;

import org.junit.jupiter.api.Test;

import com.hexaware.junittesting.MathException;
import com.hexaware.junittesting.RpnEvaluator;
import com.hexaware.junittesting.ShuntingYard;
import com.hexaware.junittesting.Tokenizer;

public class RPNTest {
	
	@Test
	void divideByZeroThrows() {
		var mc = new MathContext(10);
		var eval = new RpnEvaluator(mc);
		var expr = new ShuntingYard().toRpn(new Tokenizer().tokenize("4 / 0"));
		assertThrows(MathException.class, ()-> eval.eval(expr));
	}
	
	@Test
	
	void evaluatesSample() {
		var mc = new MathContext(10);
		var eval = new RpnEvaluator(mc);
		var expr = new ShuntingYard()
				.toRpn(new Tokenizer()
						.tokenize("3 + 4 * 2 / (1-5)"));
		var res = eval.eval(expr);
		assertThat(res.toPlainString()).isEqualTo("1"); // 3 + (8/-4) ==> 3 + (-2) = 1
		
		
		
	}

}
