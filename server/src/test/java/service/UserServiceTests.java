package service;

import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private UserService service;

    @BeforeEach
    void setup() {
        service = new UserService();
    }

    @Test
    @DisplayName("Register - Positive Case")
    void registerSuccess() {
        UserData user = new UserData("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(user));
    }

    @Test
    @DisplayName("Register - Negative Case (Register a user twice)")
    void registerDuplicate() throws AlreadyTakenException {
        UserData user = new UserData("bob", "1234", "email@gmail.com");
        service.register(user); // Register name once
        assertThrows(AlreadyTakenException.class, () -> service.register(user)); // Try twice
    }

    @Test
    @DisplayName("Clear - Positive Case")
    void clear() throws AlreadyTakenException {
        UserData user = new UserData("bob", "1234", "email@gmail.com");
        service.register(user);
        UserData user2 = new UserData("joe", "5678", "email@yahoo.com");
        service.register(user2);
        service.clear();
        assertDoesNotThrow(() -> service.register(user)); // Should not throw an error after clearing
    }
}