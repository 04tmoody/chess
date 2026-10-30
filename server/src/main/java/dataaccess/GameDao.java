package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDao {
    void clear() throws DataAccessException;
    GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;
}
