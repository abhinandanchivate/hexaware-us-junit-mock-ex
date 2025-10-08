package com.hexaware.junitesting.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.hexaware.junittesting.ShuntingYard;
import com.hexaware.junittesting.Token;
import com.hexaware.junittesting.Tokenizer;

public class ShuntingYardTest {
	
	@Test
	void precedenceWorks() {
		var t = new Tokenizer().tokenize("3 + 4 * 2 / ( 1 - 5 ) ");
		var rpn = new ShuntingYard().toRpn(t);
		// RPN : 3 4 2 * 1 5 - / +
		var s = rpn.stream().map(Token::lexeme).collect(Collectors.joining(" "));
		assertThat(s).isEqualTo("3 4 2 * 1 5 - / +");
		
		
	}

}
