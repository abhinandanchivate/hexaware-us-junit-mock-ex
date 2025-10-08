package con.hexaware.mockito.testing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.hexaware.junitesting.testing.CalculatorRepository;
import com.hexaware.junitesting.testing.CalculatorService;

public class CalculatorServiceTest {

	@Test
	void testThenReturn() {
		
		CalculatorRepository calculatorRepository = mock(CalculatorRepository.class);
		
		when(calculatorRepository.getNumber()).thenReturn(10);
		CalculatorService service = new CalculatorService(calculatorRepository);
		// act ==> repo object ==> act around repo object.
		
		int result = service.doubleIt();
		assertEquals(20, result);
		verify(calculatorRepository).getNumber(); // ensure it was called.
		
	}
	// exception.
	@Test
	void testThenThrow() {
		// create act of throwing the exception.
		// Arrangement
		CalculatorRepository calculatorRepository = mock(CalculatorRepository.class);
		
		when(calculatorRepository.getNumber()).thenThrow(new RuntimeException("DB not available")); // situation.
		CalculatorService service = new CalculatorService(calculatorRepository);
		RuntimeException ex = assertThrows(RuntimeException.class, service::doubleIt);
		assertEquals("DB not available", ex.getMessage());
		
	}
}
