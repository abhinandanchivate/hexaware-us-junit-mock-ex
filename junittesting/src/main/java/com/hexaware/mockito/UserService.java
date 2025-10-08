package com.hexaware.mockito;

public class UserService {

	private final UserRepository repository;
	
	public UserService(UserRepository repo) {
		// TODO Auto-generated constructor stub
		this.repository = repo;
	}
	
	public String getUserName(int id) {
		User user = repository.findById(id);
		return user!=null ? user.name() :"Unknown";
	}
	public void registerUser(int id, String name) {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank or null");
        }

        // Trim and create user
        User user = new User(id, name.trim());

        // Save to repository
        repository.save(user);
    }
}
