package com.hexaware.mockito;

public interface UserRepository {

	User findById(int id);
	void save(User user);
}
