package con.hexaware.mockito.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.AdditionalAnswers.answersWithDelay;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hexaware.mockito.User;
import com.hexaware.mockito.UserRepository;
import com.hexaware.mockito.UserService;

/**
 * Deep-dive Mockito examples on top of the basics.
 *
 * Class under test: UserService
 * Dependency: UserRepository (mocked)
 */
@ExtendWith(MockitoExtension.class)
class UserServiceAdvancedTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService service;

    // Simple object mother to keep tests readable
    private static User user(int id, String name) {
        return new User(id, name);
    }

    @BeforeEach
    void setUp() {
        // Example of a default, lenient stub you *might* need in larger suites.
        // Use sparingly; prefer explicit stubs in each test for clarity.
        lenient().when(repository.findById(-1)).thenReturn(null);
    }

    // ------------------------------------------------------------
    // 1) BASICS (your snippet, but with comments & BDD style)
    // ------------------------------------------------------------
    @Test
    @DisplayName("getUserName: returns user's name when found (BDD style)")
    void getUserName_found_bdd() {
        // given
        given(repository.findById(1)).willReturn(user(1, "Abhi"));

        // when
        String result = service.getUserName(1);

        // then
        assertEquals("Abhi", result);
        then(repository).should().findById(1);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("getUserName: returns 'Unknown' when not found")
    void getUserName_notFound() {
        when(repository.findById(99)).thenReturn(null);

        String result = service.getUserName(99);

        assertEquals("Unknown", result);
        verify(repository).findById(99);
        verifyNoMoreInteractions(repository);
    }

    // ------------------------------------------------------------
    // 2) ARGUMENT CAPTOR (single & multiple captures)
    // ------------------------------------------------------------
    @Test
    @DisplayName("registerUser: trims & saves (ArgumentCaptor)")
    void registerUser_trims_and_saves() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        service.registerUser(7, "  Jane  ");

        verify(repository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals(7, saved.id());
        assertEquals("Jane", saved.name());
    }

    @Test
    @DisplayName("multiple saves: capture all arguments in order")
    void registerUser_multiple_captures() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        service.registerUser(1, "John");
        service.registerUser(2, "Doe");

        verify(repository, times(2)).save(captor.capture());
        List<User> all = captor.getAllValues();
        assertEquals(List.of(user(1, "John"), user(2, "Doe")), all);
    }

    // ------------------------------------------------------------
    // 3) EXCEPTIONS & VALIDATION
    // ------------------------------------------------------------
    @ParameterizedTest(name = "blank or null name ''{0}'' should throw")
    @NullSource
    @ValueSource(strings = {"", " ", "   "})
    void registerUser_blank_throws(String bad) {
        assertThrows(IllegalArgumentException.class, () -> service.registerUser(5, bad));
        verifyNoInteractions(repository);
    }

    // ------------------------------------------------------------
    // 4) SPY (partial mock) – when to use & caveats
    // ------------------------------------------------------------
    @Test
    @DisplayName("spy: real list behavior + interaction verification")
    void spy_demo() {
        List<String> real = new ArrayList<>();
        List<String> spy = spy(real);

        spy.add("A");
        spy.add("B");

        verify(spy, times(2)).add(anyString());
        assertEquals(2, spy.size());
        assertEquals(List.of("A", "B"), spy);

        // Caveat: stubbing real methods on spies can call the real method unless you use doReturn/when
        doReturn(999).when(spy).size();
        assertEquals(999, spy.size());
    }

    // ------------------------------------------------------------
    // 5) InOrder (verify call sequence across one or more mocks)
    // ------------------------------------------------------------
    @Test
    @DisplayName("InOrder: verify repo calls happen in sequence")
    void inOrder_verification() {
        when(repository.findById(1)).thenReturn(user(1, "Abhi"));

        // perform actions
        service.getUserName(1);       // expect findById(1)
        service.registerUser(2, "Zara"); // expect save(User(2,"Zara"))

        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).findById(1);
        inOrder.verify(repository).save(user(2, "Zara"));
        inOrder.verifyNoMoreInteractions();
    }

    // ------------------------------------------------------------
    // 6) doThrow / doAnswer (void methods & custom behavior)
    // ------------------------------------------------------------
    @Test
    @DisplayName("doThrow: fail fast when persistence layer errors")
    void doThrow_on_save() {
        doThrow(new RuntimeException("DB down"))
                .when(repository).save(any(User.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.registerUser(10, "Alice"));
        assertEquals("DB down", ex.getMessage());

        verify(repository).save(user(10, "Alice"));
    }

    @Test
    @DisplayName("doAnswer: mutate arg / simulate side effects")
    void doAnswer_custom_logic() {
        // Imagine repository would uppercase names on save
        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            assertNotNull(u);
            assertEquals("Bob", u.name()); // service trims but does not uppercase
            return null; // void method
        }).when(repository).save(any(User.class));

        service.registerUser(20, "Bob");
        verify(repository).save(user(20, "Bob"));
    }

    // ------------------------------------------------------------
    // 7) Async-ish patterns: verify(timeout(...)) and delayed answers
    // ------------------------------------------------------------
    @Test
    @DisplayName("verify with timeout: polling style for eventual calls")
    void verify_with_timeout() throws Exception {
        // Simulate delayed repository call via another thread
        ExecutorService pool = Executors.newSingleThreadExecutor();

        // answersWithDelay: delay the *stubbed return*; useful to test time sensitivity
        when(repository.findById(123))
            .thenAnswer(answersWithDelay(150, invocation -> user(123, "LateUser")));

        Future<String> future = pool.submit(() -> service.getUserName(123));
        // Wait no more than 1s to be safe in CI
        String result = assertTimeoutPreemptively(Duration.ofSeconds(1), ()->future.get());
        

        assertEquals("LateUser", result);

        // If you need to verify the call happened within N ms:
        verify(repository, timeout(300)).findById(123);

        pool.shutdownNow();
    }

    // ------------------------------------------------------------
    // 8) Matchers: good usage, gotchas, and eq()
    // ------------------------------------------------------------
    @Test
    @DisplayName("Matchers: avoid mixing raw values and matchers incorrectly")
    void matchers_best_practices() {
        // GOOD: all arguments use matchers
        when(repository.findById(anyInt())).thenReturn(user(77, "X"));

        // Call SUT
        String result = service.getUserName(42);
        assertEquals("X", result);

        // GOOD: verification with eq()
        verify(repository).findById(eq(42));

        // BAD (example): mixing matchers with raw values in same call → would throw
        // when(repository.findById(anyInt())).thenReturn(user(1,"X")); // OK
        // verify(repository).findById( anyInt() ); // OK
        // But: verify(repository).findById(any()); // WRONG type
    }

    // ------------------------------------------------------------
    // 9) Reset vs fresh mocks: prefer fresh mocks per test
    // ------------------------------------------------------------
    @Test
    @DisplayName("Prefer fresh mocks per test instead of reset()")
    void prefer_fresh_mocks() {
        // This is just a guideline explanation test.
        // Avoid Mockito.reset(repository); because it erases stubs & history,
        // making tests harder to reason about. Each test already gets a fresh mock
        // courtesy of MockitoExtension (new instance per test).
        when(repository.findById(5)).thenReturn(user(5, "Fresh"));
        assertEquals("Fresh", service.getUserName(5));
        verify(repository).findById(5);
    }

    // ------------------------------------------------------------
    // 10) BDD: given/willThrow for exceptions on voids
    // ------------------------------------------------------------
    @Test
    @DisplayName("BDD: given/willThrow for void methods")
    void bdd_willThrow() {
        willThrow(new IllegalStateException("Repo locked"))
                .given(repository).save(any(User.class));

        assertThrows(IllegalStateException.class, () -> service.registerUser(9, "Neo"));
        then(repository).should().save(user(9, "Neo"));
    }
}
