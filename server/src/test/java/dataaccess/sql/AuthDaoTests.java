package dataaccess.sql;

import dataaccess.AuthDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDaoTests {
    private final AuthDao authDao = new DatabaseAuthDao();

    @BeforeEach
    void setup() {
        assertDoesNotThrow(authDao::clear);
    }

    @Test
    @DisplayName("Clear - Positive Case")
    void clearSuccess() {
        var authData = assertDoesNotThrow(() -> authDao.createAuth("bob"));
        assertDoesNotThrow(authDao::clear);
        var res = assertDoesNotThrow(() -> authDao.getAuth(authData.authToken()));
        assertNull(res);
    }

    @Test
    @DisplayName("CreateAuth - Positive Case")
    void createAuthSuccess() {
        var authData = assertDoesNotThrow(() -> authDao.createAuth("bob"));
        var res = assertDoesNotThrow(() -> authDao.getAuth(authData.authToken()));
        assertEquals("bob",res.username());
    }

    @Test
    @DisplayName("GetAuth - Positive Case")
    void getAuthSuccess() {
        var authData = assertDoesNotThrow(() -> authDao.createAuth("joe"));
        var res = assertDoesNotThrow(() -> authDao.getAuth(authData.authToken()));
        assertEquals(authData,res);
    }

    @Test
    @DisplayName("GetAuth - Negative Case (invalid auth)")
    void getAuthFailure() {
        var res = assertDoesNotThrow(() -> authDao.getAuth("fake"));
        assertNull(res);
    }

    @Test
    @DisplayName("DeleteAuth - Positive Case")
    void deleteAuthSuccess() {
        var authData = assertDoesNotThrow(() -> authDao.createAuth("joe"));
        var res = assertDoesNotThrow(() -> authDao.getAuth(authData.authToken()));
        assertEquals(authData,res);
        assertDoesNotThrow(() -> authDao.deleteAuth(authData.authToken()));
        res = assertDoesNotThrow(() -> authDao.getAuth(authData.authToken()));
        assertNull(res);
    }
}