package com.hexaware.junitesting.testing;

import org.junit.jupiter.api.Test;

import com.hexaware.junittesting.TokenType;
import com.hexaware.junittesting.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenizerTest {

	// write the test cases .
	@Test
	void parsesNumbersAndOps() {
		// we want to check the expression evaluation via tokenizer.
		var toks = new Tokenizer().tokenize("12.5 + 3*(2-1)");
		// how many tokens are there ? 9
		assertThat(toks).hasSize(9);
		// 1st token = 12.5
		assertThat(toks.get(0).type()).isEqualTo(TokenType.NUMBER);
		// second token : +
		assertThat(toks.get(1).lexeme()).isEqualTo("+");

		// fourth token : *

		assertThat(toks.get(3).lexeme()).isEqualTo("*");
	}
}
