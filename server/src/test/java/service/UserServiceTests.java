package service;

import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import model.request.*;
import model.result.*;

public class UserServiceTests {
    private UserService service;

    @BeforeEach
    void setup() {
        AuthService authService = new AuthService();
        service = new UserService(authService);
        assertDoesNotThrow(authService::clear);
        assertDoesNotThrow(service::clear);
    }

    @Test
    @DisplayName("Register - Positive Case")
    void registerSuccess() {
        RegisterRequest req = new RegisterRequest("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(req));
    }

    @Test
    @DisplayName("Register - Negative Case (Register a user twice)")
    void registerDuplicate() throws AlreadyTakenException {
        RegisterRequest req = new RegisterRequest("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(req)); // Register name once
        assertThrows(AlreadyTakenException.class, () -> service.register(req)); // Try twice
    }

    @Test
    @DisplayName("Login - Positive Case")
    void loginSuccess() {
        RegisterRequest req = new RegisterRequest("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(req));
        LoginRequest req2 = new LoginRequest("bob","1234");
        assertDoesNotThrow(() -> service.login(req2));
    }

    @Test
    @DisplayName("Login - Negative Case (Invalid Username)")
    void loginInvalidUsername() {
        LoginRequest req = new LoginRequest("bob","1234");
        assertThrows(UnauthorizedException.class, () -> service.login(req));
    }

    @Test
    @DisplayName("Login - Negative Case (Wrong Password)")
    void loginWrongPassword() {
        RegisterRequest req = new RegisterRequest("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(req));
        LoginRequest req2 = new LoginRequest("bob","5678");
        assertThrows(UnauthorizedException.class, () -> service.login(req2));
    }

    @Test
    @DisplayName("Login - Negative Case (Wrong Password)")
    void loginNull() {
        LoginRequest req = new LoginRequest(null,null);
        assertThrows(Exception.class, () -> service.login(req));
    }

    @Test
    @DisplayName("Clear - Positive Case")
    void clear() throws AlreadyTakenException {
        RegisterRequest req = new RegisterRequest("bob", "1234", "email@gmail.com");
        assertDoesNotThrow(() -> service.register(req));
        RegisterRequest req2 = new RegisterRequest("joe", "5678", "email@yahoo.com");
        assertDoesNotThrow(() -> service.register(req2));
        assertDoesNotThrow(() -> service.clear());
        assertDoesNotThrow(() -> service.register(req)); // Should not throw an error after clearing
    }
}