package service;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Assertions.assertDoesNotThrow(() -> service.register(username));
    }
}