package com.hexaware.junitesting.testing;

public class CalculatorService {
	 private final CalculatorRepository repo;

	    public CalculatorService(CalculatorRepository repo) {
	        this.repo = repo;
	    }

	    public int doubleIt() {
	        return repo.getNumber() * 2;
	    }

}
