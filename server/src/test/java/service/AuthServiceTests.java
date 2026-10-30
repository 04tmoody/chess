package service;

import static org.junit.jupiter.api.Assertions.*;

import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthServiceTests {
    private AuthService service;

    @BeforeEach
    void setup() {
        service = new AuthService();
        assertDoesNotThrow(() -> service.clear());
    }

    @Test
    @DisplayName("Register - Positive Case")
    void registerSuccess() {
        String username = "bob";
        assertDoesNotThrow(() -> service.login(username));
    }

    @Test
    @DisplayName("Logout - Positive Case")
    void logoutSuccess() {
        String username = "bob";
        AuthData auth = assertDoesNotThrow(() -> service.login(username));
        assertDoesNotThrow(() -> service.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Logout - Negative Case (no login)")
    void logoutUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> service.logout("fake"));
    }

    @Test
    @DisplayName("Logout - Negative Case (logout twice)")
    void logoutDuplicate() {
        String username = "bob";
        AuthData auth = assertDoesNotThrow(() -> service.login(username));
        assertDoesNotThrow(() -> service.logout(auth.authToken()));
        assertThrows(UnauthorizedException.class, () -> service.logout(auth.authToken()));
    }
}