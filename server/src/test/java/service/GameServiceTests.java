package service;

import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameService service;
    private String auth;

    @BeforeEach
    void setup() {
        AuthService authService = new AuthService();
        UserService userService = new UserService(authService);
        auth = assertDoesNotThrow(() -> userService.register(
                new RegisterRequest("Test User","Password","email@email.com")
        ).authToken());
        service = new GameService(authService);
        service.clear();
    }

    @Test
    @DisplayName("Create Game - Positive Case")
    void CreateGameSuccess() {
        CreateGameRequest req = new CreateGameRequest("Legendary Battle");
        assertDoesNotThrow(() -> service.createGame(req,auth));
    }

    @Test
    @DisplayName("Create Game - Negative Case (null gameName)")
    void CreateGameNull() {
        CreateGameRequest req = new CreateGameRequest(null);
        assertThrows(Exception.class, () -> service.createGame(req,auth));
    }

    @Test
    @DisplayName("Create Game - Negative Case (unauthorized)")
    void CreateGameUnauthorized() {
        CreateGameRequest req = new CreateGameRequest("Gary's Game");
        assertThrows(UnauthorizedException.class, () -> service.createGame(req,"fake"));
    }

    @Test
    @DisplayName("List Games - Positive Case")
    void ListGamesSuccess() {
        CreateGameRequest req = new CreateGameRequest("Gary's Game");
        assertDoesNotThrow(() -> service.createGame(req,auth));

        CreateGameRequest req2 = new CreateGameRequest("Bob's Game");
        assertDoesNotThrow(() -> service.createGame(req2,auth));

        CreateGameRequest req3 = new CreateGameRequest("Steve's Game");
        assertDoesNotThrow(() -> service.createGame(req3,auth));

        ListGamesResult result = assertDoesNotThrow(() -> service.listGames(auth));
        assertEquals(3,result.games().size());
        assertEquals("Gary's Game",result.games().get(0).gameName());
        assertEquals("Bob's Game",result.games().get(1).gameName());
        assertEquals("Steve's Game",result.games().get(2).gameName());
    }

    @Test
    @DisplayName("List Games - Negative Case (Unauthorized)")
    void ListGamesUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> service.listGames("hacker"));
    }

    @Test
    @DisplayName("Join Game - Positive Case")
    void JoinGameSuccess() {
        CreateGameRequest req = new CreateGameRequest("Gary's Game");
        int gameID = assertDoesNotThrow(() -> service.createGame(req,auth)).gameID();
        JoinGameRequest req2 = new JoinGameRequest("WHITE",gameID);
        assertDoesNotThrow(() -> service.joinGame(req2,auth));
        JoinGameRequest req3 = new JoinGameRequest("BLACK",gameID);
        assertDoesNotThrow(() -> service.joinGame(req3,auth));
    }

    @Test
    @DisplayName("Join Game - Negative Case (invalid gameID)")
    void JoinGameBadID() {
        JoinGameRequest req = new JoinGameRequest("WHITE",-1);
        assertThrows(Exception.class, () -> service.joinGame(req,auth));
    }

    @Test
    @DisplayName("Join Game - Negative Case (Unauthorized)")
    void JoinGameUnauthorized() {
        CreateGameRequest req = new CreateGameRequest("Gary's Game");
        int gameID = assertDoesNotThrow(() -> service.createGame(req,auth)).gameID();
        JoinGameRequest req2 = new JoinGameRequest("WHITE",gameID);
        assertThrows(UnauthorizedException.class, () -> service.joinGame(req2,"hehehe"));
    }

    @Test
    @DisplayName("Join Game - Negative Case (Game Already Taken)")
    void JoinGameDuplicate() {
        CreateGameRequest req = new CreateGameRequest("Gary's Game");
        int gameID = assertDoesNotThrow(() -> service.createGame(req,auth)).gameID();
        JoinGameRequest req2 = new JoinGameRequest("WHITE",gameID);
        assertDoesNotThrow(() -> service.joinGame(req2,auth));
        JoinGameRequest req3 = new JoinGameRequest("WHITE",gameID);
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(req3,auth));
    }
}