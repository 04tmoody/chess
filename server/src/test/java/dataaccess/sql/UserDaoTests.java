package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTests {
    private final UserDao userDao = new DatabaseUserDao();

    @BeforeEach
    void setup() {
        assertDoesNotThrow(userDao::clear);
    }

    @Test
    @DisplayName("Clear - Positive Case")
    void clearSuccess() {
        assertDoesNotThrow(() -> userDao.createUser(new UserData("bob","12345","bob@email.com")));
        assertDoesNotThrow(userDao::clear);
        var res = assertDoesNotThrow(() -> userDao.getUser("bob"));
        assertNull(res);
    }

    @Test
    @DisplayName("CreateUser - Positive Case")
    void createUserSuccess() {
        UserData bob = new UserData("bob","12345","bob@email.com");
        assertDoesNotThrow(() -> userDao.createUser(bob));
        var res = assertDoesNotThrow(() -> userDao.getUser("bob"));
        assertEquals(bob,res);
    }

    @Test
    @DisplayName("CreateUser - Negative Case (already exists")
    void createUserDuplicate() {
        UserData bob = new UserData("bob","12345","bob@email.com");
        assertDoesNotThrow(() -> userDao.createUser(bob));
        assertThrows(DataAccessException.class, () -> userDao.createUser(bob));
    }

    @Test
    @DisplayName("GetUser - Positive Case")
    void getUserSuccess() {
        UserData joe = new UserData("joe","12345","joe@email.com");
        UserData steve = new UserData("steve","54321","steve@email.com");
        assertDoesNotThrow(() -> userDao.createUser(joe));
        assertDoesNotThrow(() -> userDao.createUser(steve));
        var res = assertDoesNotThrow(() -> userDao.getUser("joe"));
        assertEquals(joe,res);
        res = assertDoesNotThrow(() -> userDao.getUser("steve"));
        assertEquals(steve,res);
    }

    @Test
    @DisplayName("GetUser - Positive Case")
    void getUserDoesNotExist() {
        UserData joe = new UserData("joe","12345","joe@email.com");
        UserData steve = new UserData("steve","54321","steve@email.com");
        assertDoesNotThrow(() -> userDao.createUser(joe));
        var res = assertDoesNotThrow(() -> userDao.getUser("steve"));
        assertNull(res);
    }
}