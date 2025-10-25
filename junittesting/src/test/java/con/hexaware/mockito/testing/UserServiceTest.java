package con.hexaware.mockito.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.hexaware.mockito.User;
import com.hexaware.mockito.UserRepository;
import com.hexaware.mockito.UserService;

public class UserServiceTest {

	@Test
    void testGetUserName() {
        // Arrange: create a mock repository
        UserRepository mockRepo = mock(UserRepository.class);// will help u to get the fake / dummy object 

        // Stub behavior: when findById(1) is called, return a User("Abhi")
        when(mockRepo.findById(1)).thenReturn(new User(1,"Abhi"));
        // when ==> then
        

        // Inject mock into service
        UserService service = new UserService(mockRepo); // mock object ==> will be treated like a UserRepo object.

        // Act
        String name = service.getUserName(1);

        // Assert
        assertEquals("Abhi", name);

        // Verify interaction
        verify(mockRepo).findById(1);
    }
}
