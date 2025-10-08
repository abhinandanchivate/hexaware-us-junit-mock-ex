package com.hexaware.junittesting;

public record Employee(int id, String name, double salary) {
	// record in java are the sp kind of class introduced in java 14  got stability in 16 .  it is a way to create immutable data carrier classes
	// DTO
	// Fields, constructor, getters ==> without get prefix just the field name) .
	// toString , equals, hashCode
	
	

}
