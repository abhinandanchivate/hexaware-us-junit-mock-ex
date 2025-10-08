package com.hexaware.mockito;

public record User(int id, String name) {
    // No extra code needed — record automatically provides:
    // - constructor
    // - getters (id(), name())
    // - equals, hashCode, toString
}
