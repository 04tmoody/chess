package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sf;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(6070);
        System.out.println("Started test HTTP server on " + port);
        sf = new ServerFacade("http://localhost:6070");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterPositive() {
        Random random = new Random();
        String newUsername = "newUser" + String.valueOf(random.nextInt(0, 10000));
        assertDoesNotThrow(() -> sf.register(newUsername, "password","email"));
    }

    @Test
    public void testRegisterDuplicate() {
        assertThrows(Exception.class,() -> {
            sf.register("alreadyExists", "password","email");
            sf.register("alreadyExists", "other","other");
        });
    }

    @Test
    public void testLoginPositive() {
        try {
            sf.register("testUserLogin", "password","email");
        } catch (Exception ignore) {}
        assertDoesNotThrow(() -> sf.login("testUserLogin","password"));
    }

    @Test
    public void testLoginBadUsername() {
        assertThrows(Exception.class, () -> sf.login("thisuserdoesnotexistoopshaha23947298","password"));
    }

    @Test
    public void testLoginBadPassword() {
        try {
            sf.register("testUserLogin", "password","email");
        } catch (Exception ignore) {}
        assertThrows(Exception.class, () -> sf.login("testUserLogin","wrong"));
    }

    @Test
    public void testLogoutPositive() {
        try {
            sf.register("testUserLogout", "password","email");
        } catch (Exception ignore) {}
        String auth = assertDoesNotThrow(() -> sf.login("testUserLogout","password").authToken());
        assertDoesNotThrow(() -> sf.logout(auth));
        assertThrows(Exception.class, () -> sf.listGames(auth));
    }

    @Test
    public void testLogoutBadAuth() {
        assertThrows(Exception.class, () -> sf.logout("fake"));
    }

    @Test
    public void createGamePositive() {
        try {
            sf.register("testUserLogout", "password","email");
        } catch (Exception ignore) {}
        String auth = assertDoesNotThrow(() -> sf.login("testUserLogout","password").authToken());
        assertDoesNotThrow(() -> sf.createGame("Test Game",auth));
    }

    @Test
    public void listGamesPositive() {
        try {
            sf.register("testUserLogout", "password","email");
        } catch (Exception ignore) {}
        String auth = assertDoesNotThrow(() -> sf.login("testUserLogout","password").authToken());
        assertDoesNotThrow(() -> sf.createGame("Test Game",auth));
        List<GameData> games = assertDoesNotThrow(() -> sf.listGames(auth));
        assertNotEquals(0,games.size());
    }

    @Test
    public void joinGamePositive() {
        Random random = new Random();
        String newGameName = "testGame" + String.valueOf(random.nextInt(0, 10000));
        try {
            sf.register("testUserLogout", "password","email");
        } catch (Exception ignore) {}
        String auth = assertDoesNotThrow(() -> sf.login("testUserLogout","password").authToken());
        assertDoesNotThrow(() -> sf.createGame(newGameName,auth));
        List<GameData> games = assertDoesNotThrow(() -> sf.listGames(auth));
        assertDoesNotThrow(() -> {
            int id = 0;
            for (GameData game:games) {
                if (game.gameName().equals(newGameName)) {
                    id=game.gameID();
                }
            }
            sf.joinGame(id,"WHITE",auth);
        });
    }

}
