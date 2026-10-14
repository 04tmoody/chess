package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class AuthServiceTests {
    private AuthService service;

    @BeforeEach
    void setup() {
        service = new AuthService();
        service.clear();
    }

    @Test
    @DisplayName("Register - Positive Case")
    void registerSuccess() {
        String username = "bob";
        assertDoesNotThrow(() -> service.login(username));
    }
}