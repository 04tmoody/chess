package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDaoTests {
    private final GameDao gameDao = new DatabaseGameDao();

    @BeforeEach
    void setup() {
        assertDoesNotThrow(gameDao::clear);
    }

    @Test
    @DisplayName("Clear - Positive Case")
    void clearSuccess() {
        assertDoesNotThrow(() -> gameDao.createGame("w","b","game",new ChessGame()));
        assertDoesNotThrow(gameDao::clear);
        int numGames = assertDoesNotThrow(() -> gameDao.listGames().size());
        assertEquals(0,numGames);
    }

    @Test
    @DisplayName("CreateGame - Positive Case")
    void createGameSuccess() {
        GameData game = assertDoesNotThrow(() -> gameDao.createGame("w","b","game",new ChessGame()));
        assertEquals("game",game.gameName());
        GameData res = assertDoesNotThrow(() -> gameDao.getGame(game.gameID()));
        assertEquals(game,res);
        assertEquals(res,new GameData(game.gameID(),"w","b","game",new ChessGame()));
    }

    @Test
    @DisplayName("GetGame - Positive Case")
    void getGameSuccess() {
        int id1 = assertDoesNotThrow(() -> gameDao.createGame("w","b","testGame",new ChessGame()).gameID());
        int id2 = assertDoesNotThrow(() -> gameDao.createGame("w","b","otherGame",new ChessGame()).gameID());
        GameData res = assertDoesNotThrow(() -> gameDao.getGame(id1));
        assertEquals(new GameData(id1,"w","b","testGame",new ChessGame()),res);
        res = assertDoesNotThrow(() -> gameDao.getGame(id2));
        assertEquals(new GameData(id2,"w","b","otherGame",new ChessGame()),res);
    }

    @Test
    @DisplayName("GetGame - Negative Case")
    void getGameFailure() {
        var game = assertDoesNotThrow(() -> gameDao.getGame(1));
        assertNull(game);
    }

    @Test
    @DisplayName("ListGames - Positive Case")
    void listGamesSuccess() {
        GameData game1 = assertDoesNotThrow(() -> gameDao.createGame("w","b","testGame",new ChessGame()));
        GameData game2 = assertDoesNotThrow(() -> gameDao.createGame("w","b","otherGame",new ChessGame()));
        List<GameData> res = assertDoesNotThrow(gameDao::listGames);
        assertEquals(2,res.size());
    }

    @Test
    @DisplayName("ListGames - Positive Case (Empty)")
    void listGamesSuccessEmpty() {
        List<GameData> res = assertDoesNotThrow(gameDao::listGames);
        assertEquals(0,res.size());
    }

    @Test
    @DisplayName("UpdateGames - Positive Case")
    void updateGamesSuccess() {
        GameData game = assertDoesNotThrow(() -> gameDao.createGame("w","b","original",new ChessGame()));
        assertEquals("original",game.gameName());
        assertDoesNotThrow(() -> gameDao.updateGame(game.gameID(),"w","b","new",new ChessGame()));
        GameData res = assertDoesNotThrow(() -> gameDao.getGame(game.gameID()));
        assertEquals("new",res.gameName());
    }
}